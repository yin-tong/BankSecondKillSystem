import Cookies from 'js-cookie'

const TokenKey = 'admin_token'
const AccountIdKey = 'admin_accountId'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getAccountId() {
  return Cookies.get(AccountIdKey)
}

export function setAccountId(accountId) {
  return Cookies.set(AccountIdKey, accountId)
}

export function removeAccountId() {
  return Cookies.remove(AccountIdKey)
}