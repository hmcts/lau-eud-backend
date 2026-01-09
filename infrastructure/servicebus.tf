locals {
  idam_topics = {
    add    = "idam-add-user"
    modify = "idam-modify-user"
    remove = "idam-remove-user"
  }
}

data "azurerm_servicebus_namespace" "idam_servicebus_namespace" {
  name                = "idam-servicebus-${var.env}"
  resource_group_name = "idam-idam-${var.env}"
}

module "servicebus-subscription" {
  for_each = local.idam_topics

  source       = "git@github.com:hmcts/terraform-module-servicebus-subscription?ref=4.x"
  name         = "idam-${each.key}-user-lau-subscription-${var.env}"
  namespace_id = data.azurerm_servicebus_namespace.idam_servicebus_namespace.id
  topic_name   = each.value
}
