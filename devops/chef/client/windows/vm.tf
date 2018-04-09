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

resource "azurerm_resource_group" "vm" {
    name = "${var.name}rg"
    location = "${var.location}"
}

resource "azurerm_virtual_network" "vm" {
    name = "${var.name}vn"
    address_space = ["10.0.0.0/16"]
    location = "${var.location}"
    resource_group_name = "${azurerm_resource_group.vm.name}"
}

resource "azurerm_subnet" "vm" {
    name = "${var.name}sub"
    resource_group_name = "${azurerm_resource_group.vm.name}"
    virtual_network_name = "${azurerm_virtual_network.vm.name}"
    address_prefix = "10.0.2.0/24"
}

resource "azurerm_public_ip" "vm" {
    name = "${var.name}ip"
    location = "${var.location}"
    resource_group_name = "${azurerm_resource_group.vm.name}"
    public_ip_address_allocation = "static"
    idle_timeout_in_minutes = 30
    domain_name_label = "${var.name}" 

    tags {
        environment = "test"
    }
}

resource "azurerm_network_interface" "vm" {
    name = "${var.name}ni"
    location = "${var.location}"
    resource_group_name = "${azurerm_resource_group.vm.name}"

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
    resource_group_name = "${azurerm_resource_group.vm.name}"
    storage_account_type = "Standard_LRS"
    create_option        = "Empty"
    disk_size_gb         = "1023"
}

resource "azurerm_virtual_machine" "vm" {
    name = "${var.name}"
    location = "${var.location}"
    resource_group_name = "${azurerm_resource_group.vm.name}"
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
        admin_username = "${var.app["user"]}"
        admin_password = "${var.app["password"]}"
    }

    os_profile_windows_config {
        provision_vm_agent = true
    }

    tags {
        environment = "test"
    }
}

resource "azurerm_virtual_machine_extension" "vm" {
    name = "chefclient"
    location = "${var.location}"
    resource_group_name = "${azurerm_resource_group.vm.name}"
    virtual_machine_name = "${azurerm_virtual_machine.vm.name}"
    publisher = "Chef.Bootstrap.WindowsAzure"
    type = "ChefClient"
    type_handler_version = "1210.12"
    settings = <<SETTINGS
        {
            "client_rb": "ssl_verify_mode :verify_none",
            "bootstrap_options": {
                "chef_node_name": "testnode1",
                "chef_server_url": "https://bizchefserver.eastus.cloudapp.azure.com/organizations/bizdevopstest",
                "validation_client_name": "bizdevopstest-validator"
            },
            "runlist": "recipe[helloworld]",
            "validation_key_format": "plaintext",
            "chef_daemon_interval": "1",
            "daemon": "service",
            "hints": {
                "public_fqdn": "${azurerm_public_ip.vm.fqdn}",
                "vm_name": "${azurerm_virtual_machine.vm.name}"
            }
        }
    SETTINGS
    protected_settings = <<SETTINGS
        {
            "validation_key": "-----BEGIN RSA PRIVATE KEY-----\nMIIEowIBAAKCAQEAuOsqDyZMObFx4yeJp2DHDap+uoe+mBTtt/tj8Kc2bg/0SoNt\n4hpsM7GKmu0dKAKdA5Rb07I9Ua+SqEFbWHPQ1tRbvHkXn0kUNvxebtBbeHZnZjSM\nV2zjLX2Gwgea6B47602PgpMnFh+4xK6rx/jJWRCVxiKuCQ7BOAHJZ2V7WSLfrwP6\noRvWw/LpsZJQWARUA/qohtyqCG4R8pFk6x5/FY2RDy382CAtKro+i8/vI7E5XHKH\nFRgydRV2IcmswOWud/z/HR05Mg45DsfQrI/Fj/J9SIYYtpgFIPEUAMHcF6rBBoKK\no0e/c23bqGF/n/BcfStWM+JXooUzF9Dpb1U3tQIDAQABAoIBAFdCsbBAdEvtTj7R\nE4YmhwgN4ouHLwt5JMxjFsAjR+4cHT1kU+AUc+bY6v080ztkME/WHJcDTyhS37QC\nU2akEHKv9zIzOlByHdXstWs5a1CnHxf9yBQhT/rRd1vsVs8eJUxeZ1BIvHMWOQyn\nGGzfG7SpvOOozdN8YPVa8n2WR+TBYKu/+I5cqNjsTPv1jocOoneF/RtKwiY5MFJm\noEMiV8k3U2CSaKqfYo3yvWKDkr7Aq3TTGwUaZyAV7ltixf3uB3OfE4Tx3DJlgJmi\nYfGi58Z1ZEIq4Kr+IBqG9O/AYR4G0oZ/uCn7OzAmtf1dxXNqg6jcRqeNa+EztEge\nB2FOtukCgYEA4dipDUWQyMcfBQuGLYjFQ5y8r6hnlNUEnsueNEVWzU8oVRwcpKUx\njdjtpGqHdj7y5aGGuu+vxPA4xYOGF2OzbKM08kOg1BVcvW9owxmAt9PAs2cF2cqD\nrOXxMupalxY7bUhepi+Hi2B7dGDEFo2d43ohCWZKDhWnHSmpbQJG5gsCgYEA0Zuc\nRREQ2O4iIsvfYcx40GoV/Ctkf6l+BiL4AJJr5v0NkS3Gnl6r9aVtepTEcnNgDra+\nkZZKA2BuT0sQAE7Gs0G/EU2Hk79J74Tsfc4W1vUvmOm0AiXAtoaqp7ueLW1P3ee+\nFhJomQkUn2+VCwaRZE5Rvdzw+wnIiNP17C6fsT8CgYALE2MHAZheIFXHG4+TABgc\nfr1KKAocZG37k6TIj3X4T598vQoykN9jQ3Y0D/1gsSmcVVWUKVkHYXiHRzH2R69u\n7noUP3jNrdaEe8g5eTC00y+qHK1Oxv26JvSzOIcEzdRQQbJSmBYfobWsOWqkaIGL\nvFuOaHPNSQaPCZIDhI8RjQKBgQCVUQLreJLF1i3fF9iTtLND1K7AqrC2+Kjb+kEt\nNyFCtXXau/9Hhi19mlD5B81Bssr08F2lHKiw6xrpxZEqhvOpcuaHjvFL4PLse4Z0\nwEEo9BTqG2GuPfKglCIxxseRmNNSQun4kziL/BoC0dwctJsSF3DHjgLk8j02q7f+\nfeAoxQKBgF5bHS4tLiZpFb0+ENkOKFHmJ3icnoU78sBqlEy9j4CvEC1Q4v1stEih\nu8NOF90NCDPX0PgZtr6xrHfPfSmPoI9gfQ50QEBUN2FHpUoYZXroSlk4iQfFBjDg\nthftyl9AtyH4+7UpTbczOlwG8CDB/TjKr/tVq19scqF7bD+lqQa0\n-----END RSA PRIVATE KEY-----"
        }
    SETTINGS
}
