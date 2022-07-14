import { login,logout } from '@/api/account'
import { getToken, setToken, removeToken, getAccountId, setAccountId, removeAccountId } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    accountId: getAccountId()
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  // SET_ACCOUNTID: (state,id) => {
  //   state.accountId = id
  // }
  SET_ACCOUNTID: (state,accountId) => {
    state.accountId = accountId
  }
  // SET_AVATAR: (state, avatar) => {
  //   state.avatar = avatar
  // }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      console.log(username.trim())
      login({ phone: username.trim(), password: password }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        commit('SET_ACCOUNTID',data.accountId)
        setToken(data.token)
        setAccountId(data.accountId)
        //getAccountId(data.accountId)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  //logout
  logout({ commit, state }) {
    console.log(state.token)
    return new Promise((resolve, reject) => {
    logout({token : state.token}).then(() => {
        removeToken() // must remove  token  first
        removeAccountId()
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        removeToken() // must remove  token  first
        removeAccountId()
        resetRouter()
        commit('RESET_STATE')
        resolve()
        store.dispatch('/account/admin/passwordLogin').then(() => {
          location.reload()
        })
        reject(error)
      })
    })
  },
  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      removeAccountId()
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

