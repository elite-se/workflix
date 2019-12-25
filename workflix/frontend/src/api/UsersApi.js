// @flow

import type { UserType } from '../datatypes/models'

class UsersApi {
  getUsers (): Promise<UserType[]> {
    // TODO implement pages
    return fetch('https://wf-backend.herokuapp.com/users?page=1')
      .then(response => response.json())
      .then(result => result.users)
  }

  getUser (id: string): Promise<?UserType> {
    return this.getUsers()
      .then((users: UserType[]) => users.find(user => user.id === id))
  }
}

export default UsersApi
