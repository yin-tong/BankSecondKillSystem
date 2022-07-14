import Cookies from 'js-cookie'

const TokenKey = 'bsks_user_token'
const AccountIDKey = 'user_accountId'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function setAccountId(accountId) {
  return Cookies.set(AccountIDKey, accountId)
}

export function getAccountId() {
  return Cookies.get(AccountIDKey)
}

export function removeAccountId() {
  return Cookies.remove(AccountIDKey)
}
