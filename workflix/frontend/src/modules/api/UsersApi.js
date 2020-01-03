// @flow

import type { UserType } from '../datatypes/User'
import { fetchChecking } from './FetchChecking'

class UsersApi {
  getUsers (): Promise<Map<string, UserType>> {
    return fetchChecking('https://wf-backend.herokuapp.com/users')
      .then(response => response.json())
      .then(result => result.users)
      .then((usersArray: UserType[]) => new Map<string, UserType>(usersArray.map(user => [user.id, user])))
  }
}

export default UsersApi
