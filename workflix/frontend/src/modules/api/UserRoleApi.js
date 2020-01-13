// @flow

import type { NewIdResultType } from './common'
import { BACKEND } from './common'
import type { UserRoleType } from '../datatypes/User'
import { safeFetch } from './SafeFetch'
import { parseDatesInUserRole } from './parseDates'

const userRolesBackend = `${BACKEND}/userRoles`

class UserRoleApi {
  getUserRoles (): Promise<Map<number, UserRoleType>> {
    return safeFetch(userRolesBackend)
      .then(response => response.json())
      .then(result => result.roles)
      .then(roles => roles.map(parseDatesInUserRole))
      .then((roles: UserRoleType[]) => new Map<number, UserRoleType>(roles.map(role => [role.id, role])))
  }

  addRoleMembership (roleId: number, memberId: string): Promise<Response> {
    return safeFetch(`${userRolesBackend}/${roleId}/members/${memberId}`, {
      method: 'PUT'
    })
  }

  removeRoleMembership (roleId: number, memberId: string): Promise<Response> {
    return safeFetch(`${userRolesBackend}/${roleId}/members/${memberId}`, {
      method: 'DELETE'
    })
  }

  addUserRole (role: ({ name: string, description: string })): Promise<NewIdResultType> {
    return safeFetch(userRolesBackend, {
      method: 'POST',
      body: JSON.stringify(role)
    })
      .then(result => result.json())
  }

  patchUserRole (role: UserRoleType): Promise<Response> {
    return safeFetch(`${userRolesBackend}/${role.id}`, {
      method: 'PATCH',
      body: JSON.stringify(role)
    })
  }

  deleteUserRole (roleId: number): Promise<Response> {
    return safeFetch(`${userRolesBackend}/${roleId}`, {
      method: 'DELETE'
    })
  }
}

export default UserRoleApi
