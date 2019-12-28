// @flow

import type { UserType } from '../datatypes/models'

class UsersApi {
  getUsers (): Promise<Map<string, UserType>> {
    // TODO implement pages
    return fetch('https://wf-backend.herokuapp.com/users?page=1')
      .then(response => response.json())
      .then(result => result.users)
      .then((usersArray: UserType[]) => new Map<string, UserType>(usersArray.map(user => [user.id, user])))
  }
}

export default UsersApi
