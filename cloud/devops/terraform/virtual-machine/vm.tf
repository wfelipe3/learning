variable location {
    description = "azure region for this virtual machine"
}

variable "subscription_id" {
    description = "Azure subscription id"
}

variable "client_id" {
    description = "Azure client id"
}

variable "client_secret" {
    description = "Azure subscription client secret"
}

variable "tenant_id" {
    description = "Azure tenant id"
}

variable vmpassword {
    description = "virtual machine password"
}

variable "name" {
    description = "Virtual machine and resources name"
}

provider "azurerm" {
    subscription_id = "${var.subscription_id}"
    client_id       = "${var.client_id}"
    client_secret   = "${var.client_secret}"
    tenant_id       = "${var.tenant_id}"
}

resource "azurerm_resource_group" "vm" {
    name = "${var.name}rg"
    location = "${var.location}"
}

resource "azurerm_virtual_network" "vm" {
    name = "${var.name}vn"
    address_space = ["10.0.0.0/16"]
    location = "${var.location}"
    resource_group_name = "${var.name}rg"
}

resource "azurerm_subnet" "vm" {
    name = "${var.name}sub"
    resource_group_name = "${var.name}rg"
    virtual_network_name = "${azurerm_virtual_network.vm.name}"
    address_prefix = "10.0.2.0/24"
}

resource "azurerm_public_ip" "vm" {
    name = "${var.name}ip"
    location = "${var.location}"
    resource_group_name = "${var.name}rg"
    public_ip_address_allocation = "Dynamic"
    idle_timeout_in_minutes = 30

    tags {
        environment = "test"
    }
}

resource "azurerm_network_interface" "vm" {
    name = "${var.name}ni"
    location = "${var.location}"
    resource_group_name = "${var.name}rg"

    ip_configuration {
        name = "testconfiguration1"
        subnet_id = "${azurerm_subnet.vm.id}"
        private_ip_address_allocation = "dynamic"
        public_ip_address_id = "${azurerm_public_ip.vm.id}"
    }
}

resource "azurerm_managed_disk" "vm" {
    name                 = "${var.name}datadisk_existing"
    location             = "${var.location}"
    resource_group_name  = "${var.name}rg"
    storage_account_type = "Standard_LRS"
    create_option        = "Empty"
    disk_size_gb         = "1023"
}

resource "azurerm_virtual_machine" "vm" {
    name = "${var.name}"
    location = "${var.location}"
    resource_group_name = "${var.name}rg"
    network_interface_ids = ["${azurerm_network_interface.vm.id}"]
    vm_size = "Standard_DS1_v2"
    delete_os_disk_on_termination = true
    delete_data_disks_on_termination = true

    storage_image_reference {
        publisher = "MicrosoftWindowsServer"
        offer = "WindowsServer"
        sku = "2016-Datacenter"
        version = "latest"
    }

    storage_os_disk {
        name = "myosdisk1"
        caching = "ReadWrite"
        create_option = "FromImage"
        managed_disk_type = "Standard_LRS"
    }

    os_profile {
        computer_name = "hostname"
        admin_username = "bizagi"
        admin_password = "${var.vmpassword}"
    }

    os_profile_windows_config {
        provision_vm_agent = true
    }

    tags {
        environment = "test"
    }
}

resource "azurerm_virtual_machine_extension" "vm" {
    name = "chocolatey"
    location = "${var.location}"
    resource_group_name = "${var.name}rg"
    virtual_machine_name = "${azurerm_virtual_machine.vm.name}"
    publisher = "Microsoft.Compute"
    type = "CustomScriptExtension"
    type_handler_version = "1.9"
    settings = <<SETTINGS
        {
            "fileUris": [
                "https://scriptsstaccount.blob.core.windows.net/scripts/install.ps1"
            ],
            "commandToExecute": "powershell.exe -File install.ps1"
        }
    SETTINGS
}
