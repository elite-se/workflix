// @flow

import { safeFetch } from './SafeFetch'
import { BACKEND } from './common'

const loginBackend = `${BACKEND}/login`

class LoginApi {
  getToken (email: string, password: string): Promise<string> {
    return safeFetch(loginBackend, {
      body: JSON.stringify({ email, password })
    })
      .then(response => response.json())
      .then(result => result.token)
  }
}

export default LoginApi
