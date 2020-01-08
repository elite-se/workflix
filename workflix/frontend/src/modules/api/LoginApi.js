// @flow

import { safeFetch } from './SafeFetch'
import { BACKEND } from './common'

const loginBackend = `${BACKEND}/login`

class LoginApi {
  login (email: string, password: string): Promise<string> {
    return safeFetch(loginBackend, {
      method: 'POST',
      body: JSON.stringify({ email, password })
    })
      .then(response => response.json())
      .then(result => result.token)
  }

  logout (): Promise<Response> {
    return safeFetch(loginBackend, {
      method: 'DELETE'
    })
  }
}

export default LoginApi
