provider "azurerm" {
  subscription_id = "d3d6084b-d7fd-4800-92d8-7d24332fb61e"
  client_id       = "4427a02b-89d1-4f08-8265-ab0eb2c801ec"
  client_secret   = "dhNtOkTiFl55mraNBLPzldnZWQeKEYs8ty+8w9mvP8s="
  tenant_id       = "96973449-0a60-448a-b1ad-3c7237cd6563"
}

resource "azurerm_resource_group" "test" {
  name = "tf-test"
  location = "East US"
}

resource "azurerm_application_insights" "test" {
  name = "tf-test-insights"
  location "East US"
  resource_group_name = "${azurerm_resource_group.test.name}"
  application_type = "Web"
}

output instrumentation_key {
  value = "${azurerm_application_insights.test.instrumentation_key}
}

output "app_id" {
  value = "${azurerm_application_insights.test.app_id}"
}
