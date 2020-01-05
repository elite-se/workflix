// @flow

import type { UserRoleType, UserType } from '../datatypes/User'
import { safeFetch } from './SafeFetch'
import { BACKEND } from './common'
import { parseDatesInUserRole } from './parseDates'

class UsersApi {
  getUsers (): Promise<Map<string, UserType>> {
    return safeFetch(`${BACKEND}/users`)
      .then(response => response.json())
      .then(result => result.users)
      .then((usersArray: UserType[]) => new Map<string, UserType>(usersArray.map(user => [user.id, user])))
  }

  getUserRoles (): Promise<Map<number, UserRoleType>> {
    return safeFetch(`${BACKEND}/userRoles`)
      .then(response => response.json())
      .then(result => result.roles)
      .then(roles => roles.map(parseDatesInUserRole))
      .then((roles: UserRoleType[]) => new Map<number, UserRoleType>(roles.map(role => [role.id, role])))
  }
}

export default UsersApi
