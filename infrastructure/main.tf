provider "azurerm" {
  features {}
}

provider "azurerm" {
  subscription_id            = var.aks_subscription_id
  skip_provider_registration = "true"
  features {}
  alias = "postgres_network"

}

locals {
  db_connection_options = "?sslmode=require"
  vault_name            = "${var.product}-${var.env}"
  asp_name              = "${var.product}-${var.env}"
  env                   = var.env
  db_server_name        = "${var.product}-${var.component}-flexible"
}

data "azurerm_key_vault" "key_vault" {
  name                = local.vault_name
  resource_group_name = local.vault_name
}

module "lau-eud-db-flexible" {
  providers = {
    azurerm.postgres_network = azurerm.postgres_network
  }

  source        = "git@github.com:hmcts/terraform-module-postgresql-flexible?ref=master"
  env           = var.env
  subnet_suffix = var.subnet_suffix

  product       = var.product
  component     = var.component
  business_area = "cft"
  name          = local.db_server_name

  common_tags = var.common_tags

  pgsql_storage_mb          = var.pgsql_storage_mb
  pgsql_delegated_subnet_id = var.pgsql_delegated_subnet_id

  auto_grow_enabled = true

  pgsql_admin_username = "lauadmin"
  pgsql_version        = "15"

  # Setup Access Reader db user
  force_user_permissions_trigger = "1"

  pgsql_databases = [
    {
      name : var.lau_eud_db_name
    }
  ]

  pgsql_server_configuration = [
    {
      name  = "azure.extensions"
      value = "pg_stat_statements,pg_buffercache,pgcrypto,hypopg"
    }
  ]

  admin_user_object_id = var.jenkins_AAD_objectId

  action_group_name          = join("-", [var.db_monitor_action_group_name, local.db_server_name, var.env])
  email_address_key          = var.db_alert_email_address_key
  email_address_key_vault_id = data.azurerm_key_vault.key_vault.id
}

////////////////////////////////
// Populate Vault with DB info
////////////////////////////////

resource "azurerm_key_vault_secret" "POSTGRES-USER" {
  key_vault_id = data.azurerm_key_vault.key_vault.id
  name         = "${var.component}-POSTGRES-USER"
  value        = module.lau-eud-db-flexible.username
}

resource "azurerm_key_vault_secret" "POSTGRES-PASS" {
  key_vault_id = data.azurerm_key_vault.key_vault.id
  name         = "${var.component}-POSTGRES-PASS"
  value        = module.lau-eud-db-flexible.password
}

resource "azurerm_key_vault_secret" "POSTGRES_HOST" {
  key_vault_id = data.azurerm_key_vault.key_vault.id
  name         = "${var.component}-POSTGRES-HOST"
  value        = module.lau-eud-db-flexible.fqdn
}

resource "azurerm_key_vault_secret" "POSTGRES_PORT" {
  key_vault_id = data.azurerm_key_vault.key_vault.id
  name         = "${var.component}-POSTGRES-PORT"
  value        = var.postgresql_flexible_server_port
}

resource "azurerm_key_vault_secret" "POSTGRES_DATABASE" {
  key_vault_id = data.azurerm_key_vault.key_vault.id
  name         = "${var.component}-POSTGRES-DATABASE"
  value        = var.lau_eud_db_name
}

# Copy postgres password for flyway migration
resource "azurerm_key_vault_secret" "flyway_password" {
  key_vault_id = data.azurerm_key_vault.key_vault.id
  name         = "eud-flyway-password"
  value        = module.lau-eud-db-flexible.password
}

resource "azurerm_key_vault_secret" "lau_eud_db_user" {
  key_vault_id = data.azurerm_key_vault.key_vault.id
  name         = "eud-backend-app-db-user-flexible"
  value        = "lauuser"
}

resource "azurerm_key_vault_secret" "eud_request_key" {
  key_vault_id = data.azurerm_key_vault.key_vault.id
  name         = "eud-backend-encryption-key"
  value        = random_password.password.result
}

resource "random_password" "password" {
  length           = 32
  override_special = "()-_"
}
