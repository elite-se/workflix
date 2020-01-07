// @flow

import type { UserRoleType, UserType } from '../datatypes/User'
import { safeFetch } from './SafeFetch'
import { BACKEND } from './common'
import { parseDatesInUserRole } from './parseDates'
import ProcessGroupsApi from './ProcessGroupsApi'

class UsersApi {
  getUsers (): Promise<Map<string, UserType>> {
    return safeFetch(`${BACKEND}/users`)
      .then(response => response.json())
      .then(result => result.users)
      .then(users => Promise.all([
        Promise.resolve(users),
        this.getUserRoles(),
        new ProcessGroupsApi().getProcessGroups()
      ]))
      // TODO remove the following block once the API delivers all the necessary information
      .then(([users, roles, groups]) => users.map(user => ({
        ...user,
        userRoleIds: Array.from(roles.values())
          .filter(role => role.memberIds.includes(user.id))
          .map(role => role.id),
        processGroupIds: Array.from(groups.values())
          .filter(group => group.membersIds.includes(user.id))
          .map(group => group.id)
      })))
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
