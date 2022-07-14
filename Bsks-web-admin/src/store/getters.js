const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.account.token,
  accountId: state => state.account.accountId,
  avatar: state => state.user.avatar,
  name: state => state.user.name
}
export default getters
