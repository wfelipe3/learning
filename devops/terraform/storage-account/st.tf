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

variable "name" {
    description = "Resources base name"
}

variable "location" {
    description = "Azure resources region"
}

variable "file" {
    description = "File to be uploaded to blob storage"
}

provider "azurerm" {
    subscription_id = "${var.subscription_id}"
    client_id = "${var.client_id}"
    client_secret = "${var.client_secret}"
    tenant_id = "${var.tenant_id}"
}

resource "azurerm_resource_group" "st" {
    name = "${var.name}rg"
    location = "${var.location}"
}

resource "azurerm_storage_account" "st" {
    name = "${var.name}staccount"
    resource_group_name = "${azurerm_resource_group.st.name}"
    location = "${var.location}"
    account_tier = "Standard"
    account_replication_type = "LRS"
}

resource "azurerm_storage_container" "st" {
    name = "scripts"
    resource_group_name = "${azurerm_resource_group.st.name}"
    storage_account_name = "${azurerm_storage_account.st.name}"
    container_access_type = "blob"
}

resource "azurerm_storage_blob" "st" {
    name = "install.ps1"
    resource_group_name = "${azurerm_resource_group.st.name}"
    storage_account_name = "${azurerm_storage_account.st.name}"
    storage_container_name = "${azurerm_storage_container.st.name}"
    type = "block"
    source = "${var.file}"
}

output "url" {
    value = "${azurerm_storage_blob.st.url}"
}
