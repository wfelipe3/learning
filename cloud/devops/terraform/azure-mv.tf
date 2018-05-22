provider "azurerm" {
}

resource "azurerm_resource_group" "gateway" {
  name = "gateway"
  location = "East US"
}

resource "azurerm_public_ip" "gateway" {
  name = "gatewayip" 
  location = "East US"
  resource_group_name = "${azurerm_resource_group.gateway.name}"
  public_ip_address_allocation = "static"

  tags {
    environment = "test"
  }
}

resource "azurerm_virtual_network" "gateway" {
  name = "gatewayvn"
  address_space = ["10.0.0.0/16"]  
  location = "East US"
  resource_group_name = "${azurerm_resource_group.gateway.name}"
}

resource "azurerm_subnet" "gateway" {
  name = "gatewayvn"
  resource_group_name = "${azurerm_resource_group.gateway.name}"
  virtual_network_name = "${azurerm_virtual_network.gateway.name}"
  address_prefix = "10.0.2.0/24"
}

resource "azurerm_network_interface" "gateway" {
  name = "gatewayni"
  location = "East US"
  resource_group_name = "${azurerm_resource_group.gateway.name}"

  ip_configuration {
    name  = "gatewayconfiguration"
    subnet_id = "${azurerm_subnet.gateway.id}"
    private_ip_address_allocation = "dynamic"
    public_ip_address_id = "${azurerm_public_ip.gateway.id}"
  }
}

resource "azurerm_managed_disk" "gateway" {
  name = "datadisk_existing"
  location = "East US"
  resource_group_name = "${azurerm_resource_group.gateway.name}"
  storage_account_type = "Standard_LRS"
  create_option = "Empty"
  disk_size_gb = "1023"
}

resource "azurerm_virtual_machine" "gateway" {
  name = "gatewayvm"
  location = "East US"
  resource_group_name = "${azurerm_resource_group.gateway.name}"
  network_interface_ids = ["${azurerm_network_interface.gateway.id}"]
  vm_size = "Standard_DS1_v2"
  delete_os_disk_on_termination = true
  delete_data_disks_on_termination = true

  os_profile_windows_config {
    provision_vm_agent = false
  }
  
  storage_image_reference {
    publisher = "MicrosoftWindowsServer"
    offer  = "WindowsServer"
    sku = "2016-Datacenter"
    version = "latest"
  }

  storage_os_disk {
    name  = "gatewayosdisk1"
    caching = "ReadWrite"
    create_option = "FromImage"
    managed_disk_type = "Standard_LRS"
  }

  os_profile {
    computer_name = "hostname"
    admin_username = "testadmin"
    admin_password = "Bizagi123*/"
  }

  tags {
    environment = "test"
    other = "value"
  }

}

resource "azurerm_virtual_machine_extension" "chocolatey" {
  name = "chocolatey"
  location = "East US"
  resource_group_name = "${azurerm_resource_group.gateway.name}"
  virtual_machine_name = "${azurerm_virtual_machine.gateway.name}"
  publisher = "Microsoft.Compute"
  type = "CustomScriptExtension"
  type_handler_version = "1.4"
  settings = <<SETTINGS
    {
      "commandToExecute": "iex ((new-object net.webclient).DownloadString('https://chocolatey.org/install.ps1'))\nchoco install googlechrome -y"
    }
  SETTINGS
}

