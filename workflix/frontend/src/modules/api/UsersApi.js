// @flow

import type { UserType } from '../datatypes/User'
import { safeFetch } from './SafeFetch'
import { BACKEND } from './common'

const usersBackend = `${BACKEND}/users`
const usersCreationRequestBackend = `${BACKEND}/userCreationRequest`

class UsersApi {
  getUsers (): Promise<Map<string, UserType>> {
    return safeFetch(usersBackend)
      .then(response => response.json())
      .then(result => result.users)
      .then((usersArray: UserType[]) => new Map<string, UserType>(usersArray.map(user => [user.id, user])))
  }

  sendVerificationMail (email: string): Promise<Response> {
    return safeFetch(usersCreationRequestBackend, {
      method: 'POST',
      body: JSON.stringify({ email })
    })
  }

  createUser (email: string, password: string, verificationCode: string, name: string, displayName: string): Promise<Response> {
    return safeFetch(`${usersBackend}/${verificationCode}`, {
      method: 'PUT',
      body: JSON.stringify({ email, password, name, displayName })
    })
  }
}

export default UsersApi
