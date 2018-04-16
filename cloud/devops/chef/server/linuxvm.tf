variable name {
    description = "Azure resources base name"
}

variable location {
    description = "Azure resources location"
}

variable app {
    type = "map"
    description = "app variables"    
}

variable credential {
    type = "map"
    description = "Azure app credential to create resources"
}

provider "azurerm" {
    subscription_id = "${var.credential["subscription_id"]}"
    client_id       = "${var.credential["client_id"]}"
    client_secret   = "${var.credential["client_secret"]}"
    tenant_id       = "${var.credential["tenant_id"]}"
}

resource "azurerm_resource_group" "test" {
  name     = "${var.name}rg"
  location = "${var.location}"
}

resource "azurerm_virtual_network" "test" {
  name                = "${var.name}vn"
  address_space       = ["10.0.0.0/16"]
  location            = "${azurerm_resource_group.test.location}"
  resource_group_name = "${azurerm_resource_group.test.name}"
}

resource "azurerm_subnet" "test" {
  name                 = "${var.name}sub"
  resource_group_name  = "${azurerm_resource_group.test.name}"
  virtual_network_name = "${azurerm_virtual_network.test.name}"
  address_prefix       = "10.0.2.0/24"
}

resource "azurerm_public_ip" "test" {
    name = "${var.name}ip"
    location = "${azurerm_resource_group.test.location}"
    resource_group_name = "${azurerm_resource_group.test.name}"
    public_ip_address_allocation = "static"
    idle_timeout_in_minutes = 30
    domain_name_label = "${var.name}" 

    tags {
        environment = "test"
    }
}

resource "azurerm_network_interface" "test" {
  name                = "${var.name}ni"
  location            = "${azurerm_resource_group.test.location}"
  resource_group_name = "${azurerm_resource_group.test.name}"

  ip_configuration {
    name                          = "${var.name}ipconfig"
    subnet_id                     = "${azurerm_subnet.test.id}"
    private_ip_address_allocation = "dynamic"
    public_ip_address_id = "${azurerm_public_ip.test.id}"
  }
}

resource "azurerm_virtual_machine" "test" {
  name                  = "${var.name}vm"
  location              = "${azurerm_resource_group.test.location}"
  resource_group_name   = "${azurerm_resource_group.test.name}"
  network_interface_ids = ["${azurerm_network_interface.test.id}"]
  vm_size               = "Standard_DS1_v2"

  delete_os_disk_on_termination = true
  delete_data_disks_on_termination = true

  storage_image_reference {
    publisher = "Canonical"
    offer     = "UbuntuServer"
    sku       = "16.04-LTS"
    version   = "latest"
  }

  storage_os_disk {
    name              = "chefserverdisk"
    caching           = "ReadWrite"
    create_option     = "FromImage"
    managed_disk_type = "Standard_LRS"
  }

  os_profile {
    computer_name  = "${var.app["user"]}"
    admin_username = "${var.app["user"]}"
  }

  os_profile_linux_config {
    disable_password_authentication = true
    ssh_keys = {
        path = "/home/${var.app["user"]}/.ssh/authorized_keys"
        key_data = "${file(var.app["sshcert"])}"
    }
  }

  tags {
    environment = "staging"
  }
}

resource "null_resource" "configure_chef" {
  depends_on = ["azurerm_virtual_machine.test"]

  provisioner "remote-exec" {
    inline = [
      "wget -P /tmp https://packages.chef.io/files/stable/chef-server/12.17.33/ubuntu/16.04/chef-server-core_12.17.33-1_amd64.deb",
      "sudo dpkg -i /tmp/chef-server-core_12.17.33-1_amd64.deb",
      "sudo chef-server-ctl install chef-manage",
      "sudo chef-server-ctl reconfigure",
      "sudo chef-server-ctl user-create feliperojas felipe rojas william.rojas@bizagi.com 'Bizagi123*/' --filename ~/auth.pem",
      "sudo chef-server-ctl org-create bizdevopstest 'bizagi devops test' --association_user feliperojas --filename ~/bizdevopstest.pem",
    ]

    connection {
      type = "ssh"
      user = "${var.app["user"]}"
      host = "${azurerm_public_ip.test.fqdn}"
      private_key = "${file(var.app["privatekey"])}"
    }
  }

  provisioner "file" {
    content = <<CHEFSERVERFILE
      server_name = "${azurerm_public_ip.test.fqdn}"
      api_fqdn = server_name
      bookshelf['vip'] = server_name
      bookshelf['api_fqdn'] = server_name
      bookshelf['web_ui_fqdn'] = server_name
      nginx['url'] = "https://#{server_name}"
      nginx['server_name'] = server_name
      #nginx['ssl_certificate'] = "/var/opt/chef-server/nginx/ca/#{server_name}.crt"
      #nginx['ssl_certificate_key'] = "/var/opt/chef-server/nginx/ca/#{server_name}.key"
      lb['fqdn'] = server_name
      lb['api_fqdn'] = server_name
      lb['web_ui_fqdn'] = server_name
    CHEFSERVERFILE
    destination = "/home/chefserver/chef-server.rb"

    connection {
      type = "ssh"
      user = "${var.app["user"]}"
      host = "${azurerm_public_ip.test.fqdn}"
      private_key = "${file(var.app["privatekey"])}"
    }
  }

  provisioner "remote-exec" {
    inline = [
      "echo '${azurerm_public_ip.test.ip_address} ${azurerm_public_ip.test.fqdn} ${var.name}' | sudo tee -a /etc/hosts",
      "echo $(ifconfig eth0 | grep 'inet ' | awk -F'[: ]+' '{ print $4 }') ${azurerm_public_ip.test.fqdn} ${var.name} | sudo tee -a /etc/hosts",
      "sudo cp /home/chefserver/chef-server.rb /etc/opscode/chef-server.rb",
      "sudo chef-server-ctl reconfigure"
    ]

    connection {
      type = "ssh"
      user = "${var.app["user"]}"
      host = "${azurerm_public_ip.test.fqdn}"
      private_key = "${file(var.app["privatekey"])}"
    }
  }
}