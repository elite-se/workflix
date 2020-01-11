// @flow

import type { UserRoleType, UserType } from '../datatypes/User'
import { safeFetch } from './SafeFetch'
import { BACKEND } from './common'
import { parseDatesInUserRole } from './parseDates'

const usersBackend = `${BACKEND}/users`

class UsersApi {
  getUsers (): Promise<Map<string, UserType>> {
    return safeFetch(usersBackend)
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

  addRoleMembership (roleId: number, memberId: string): Promise<Response> {
    return safeFetch(`${BACKEND}/userRoles/${roleId}/members/${memberId}`, {
      method: 'PUT'
    })
  }

  removeRoleMembership (roleId: number, memberId: string): Promise<Response> {
    return safeFetch(`${BACKEND}/userRoles/${roleId}/members/${memberId}`, {
      method: 'DELETE'
    })
  }

  patchRole (role: UserRoleType): Promise<Response> {
    return safeFetch(`${BACKEND}/userRoles/${role.id}`, {
      method: 'PATCH',
      body: JSON.stringify(role)
    })
  }

  sendVerificationMail (email: string): Promise<Response> {
    return safeFetch(usersBackend, {
      method: 'POST',
      body: JSON.stringify({ email })
    })
  }

  createUser (email: string, password: string, verificationCode: string): Promise<Response> {
    return safeFetch(`${usersBackend}/${verificationCode}`, {
      method: 'POST',
      body: JSON.stringify({ email, password })
    })
  }
}

export default UsersApi
