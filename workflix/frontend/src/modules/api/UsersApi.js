// @flow

import type { UserRoleType, UserType } from '../datatypes/User'
import { safeFetch } from './SafeFetch'

class UsersApi {
  getUsers (): Promise<Map<string, UserType>> {
    return safeFetch('https://wf-backend.herokuapp.com/users')
      .then(response => response.json())
      .then(result => result.users)
      .then((usersArray: UserType[]) => new Map<string, UserType>(usersArray.map(user => [user.id, user])))
  }

  getUserRoles (): Promise<Map<number, UserRoleType>> {
    return safeFetch('https://wf-backend.herokuapp.com/userRoles')
      .then(response => response.json())
      .then(result => result.roles)
      .then((roles: UserRoleType[]) => new Map<number, UserRoleType>(roles.map(role => [role.id, role])))
  }
}

export default UsersApi
