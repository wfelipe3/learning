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

resource "azurerm_public_ip" "vm" {
    name = "${var.name}ip"
    location = "${azurerm_resource_group.test.location}"
    resource_group_name = "${azurerm_resource_group.test.name}"
    public_ip_address_allocation = "Dynamic"
    idle_timeout_in_minutes = 30

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
    public_ip_address_id = "${azurerm_public_ip.vm.id}"
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
    name              = "myosdisk1"
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
