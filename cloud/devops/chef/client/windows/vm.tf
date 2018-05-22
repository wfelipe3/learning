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

variable chef {
    type = "map"
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

output "fqdn" {
    value = "${azurerm_public_ip.vm.fqdn}"
}
output "name" {
    value = "${azurerm_virtual_machine.vm.name}"
}

/*resource "azurerm_virtual_machine_extension" "vm" {
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
                "chef_node_name": "${var.chef["chef_node_name"]}",
                "chef_server_url": "${var.chef["chef_server_url"]}",
                "validation_client_name": "${var.chef["validation_client_name"]}"
            },
            "runlist": "${var.chef["run_list"]}",
            "validation_key_format": "plaintext",
            "chef_daemon_interval": "${var.chef["chef_deamon_interval"]}",
            "daemon": "service",
            "hints": {
                "public_fqdn": "${azurerm_public_ip.vm.fqdn}",
                "vm_name": "${azurerm_virtual_machine.vm.name}"
            }
        }
    SETTINGS
    protected_settings = <<SETTINGS
        {
            "validation_key": "-----BEGIN RSA PRIVATE KEY-----\nMIIEpAIBAAKCAQEAubst0oTYBQN2bGD9LTadDOv6cmO1jPnQV5srGpDhupZF6iBr\n8bjW60zQUrR6PZHAKoUXAVujln2yr9sDwrDtlyZaruxzQX7H51UHPsHQ77BGXTDn\nL2qY7Sd0HkJcx2sKKE7KtgXajwxHUDmz6SjuatF9IH2/JMK39fodKbkWHGnuqKq8\nsyaqDU4a2AEPrYWbnI/0sj9yjkV8MgYtOTMZluaeHM/LRdTywN6cqgpR0G+k1xxs\nJ7I/wX/zZsrsxBdAUySuvtpZoL9qmOPMrYHPO0XS6kdfP41zl7exZstr6kBKyPN7\njI0G4xgQKrDjLQxeiOM33+zEgGgtoCox12PITwIDAQABAoIBAH8h/Q+pFyT3Zcxx\n51tXktERhnFehxtktJQDBtI8cgcCbgEBCGKBn7uY7NBv8WzLo7p3N3QagjO0/ucZ\nB83wxfE8s74g5BPk8HkM3tS6XoZzJvcLTnRKhztuUjGUQ2XNgzpsFJ3cC+r3AKmN\nlfT3q5s6omfnKzvCfaHF/shchSIUxO2+z+AaoLWZuqO5JSgWqWRyAoyE0WzhI7ET\n8PqANoSe+adqGXwAR/SRYJn0z1IZwt+2ARWxI7ZmNvD/skgVlIcSPO3atAH6iEKe\nop9fg/5njBHOZaWZReTO59dTvC0JgqU3aqDnVCAmSKjzBWPBHXpa94l1XMzM66JV\ntA6bvkECgYEA8JbD/37XeRQSQJ1L4l/L/1IEHEVotnaqx4Ji0LBdDMfGtauXt6Ac\naITtPEAEZoHuax6YQju4XAwKAUe3P+jm/F8FCkC/YU+JsYE+7nUlldR9Ho/DRM2j\nFBpae0PpMdM2KG6gHDge80Y6wE8mu2ThtajxWOsNvdhiB5uM18SMrKkCgYEAxaDX\nwW71l6VsbRB608VQgi7FDfY8TUhHXNwPjDSNusnX2rW5Fl/iYZ7vIghudSPEa9zC\nhXsb3sfqmtBydItXi+Y02TLZJn3SQT8MigwQgxj4iO4B26Yr9OaLXPOW5u2rEDaF\nl3s5V2NIOhu0/M5z1anduiKE6qibkZqZtXJTMDcCgYEAiyeIwfSJiJyVWg4g4BRs\nl4bGndt/j1nfuXmu0enQSB4czuMq46iWBdYsqVaVtPyd/BM8GcMVBRhpiQgD89Ew\nGPSUo7ODfjNU/vg0gBP940V+APlCBj+yfWQoxXcoIAt8HbKupOPI8wjB3o1pZ6YX\n7syCm92Imy/Ws3PC6OHkQ9ECgYEAnth9EpjhBZaM48zPLM/3uetlr6cvKN7jnRuz\n03maQXxi5wQRVI6VIungQ3aLcdPh2OCD6U3eRN5jDzRkCpDFOUk7SSi5qVqQWKkY\npwyzaOv1N/o1vBqAUkPL2FZiABMf39Qy5GLC4B6Iu0vx2REHwcDa2vVL6GDFSe79\nhP3zJ1kCgYAhW5G4AcHiRhnFKQeQeidLIoh+ksFQ6tC+Rso1EJ/4pEgSVUk3xTEP\np0vG5GV/yMvAKwz2ON3qW5YhTCRetrQ0ajWs2WRpleyn0Hbybo0RAf3vFa9Jcdef\nUt/jlAI5/lAEgAx9CzlKb4eQRxKOJvPWLpYoNiLtVax1dQ+s33LvrA==\n-----END RSA PRIVATE KEY-----"
        }
    SETTINGS
}
*/