// @flow

import type { UserType } from '../datatypes/User'
import { safeFetch } from './SafeFetch'
import { BACKEND } from './common'

const usersBackend = `${BACKEND}/users`

class UsersApi {
  getUsers (): Promise<Map<string, UserType>> {
    return safeFetch(usersBackend)
      .then(response => response.json())
      .then(result => result.users)
      .then((usersArray: UserType[]) => new Map<string, UserType>(usersArray.map(user => [user.id, user])))
  }
}

export default UsersApi
