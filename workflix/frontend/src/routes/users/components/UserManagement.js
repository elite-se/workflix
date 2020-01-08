// @flow

import React from 'react'
import withPromiseResolver from '../../../modules/app/hocs/withPromiseResolver'
import type { UserRoleType, UserType } from '../../../modules/datatypes/User'
import UsersApi from '../../../modules/api/UsersApi'
import ProcessGroupsApi from '../../../modules/api/ProcessGroupsApi'
import type { ProcessGroupType } from '../../../modules/datatypes/ProcessGroup'
import UserCards from './users/UserCards'
import ProcessGroupCards from './groups/ProcessGroupCards'
import RoleCards from './roles/RoleCards'

type PropsType = {|
  users: Map<string, UserType>,
  processGroups: Map<number, ProcessGroupType>,
  roles: Map<number, UserRoleType>
|}

type StateType = {|
  mode: 'USERS' | 'GROUPS' | 'ROLES'
|}

class UserManagement extends React.Component<PropsType, StateType> {
  state = { mode: 'USERS' }

  onProcessGroupSelected = (group: ProcessGroupType) => {
    this.setState({ mode: 'GROUPS' })
  }

  onRoleSelected = (role: UserRoleType) => {
    this.setState({ mode: 'ROLES' })
  }

  onUserSelected = (user: UserType) => {
    this.setState({ mode: 'USERS' })
  }

  render () {
    const { processGroups, roles, users } = this.props
    switch (this.state.mode) {
      case 'USERS':
        return <UserCards users={users} processGroups={processGroups} roles={roles}
                          onRoleSelected={this.onRoleSelected} onProcessGroupSelected={this.onProcessGroupSelected}/>
      case 'ROLES':
        return <RoleCards roles={roles} users={users} onUserSelected={this.onUserSelected}/>
      case 'GROUPS':
        return <ProcessGroupCards processGroups={processGroups} users={users} onUserSelected={this.onUserSelected}/>
    }
    return null
  }
}

const promiseCreator = () => Promise.all([
  new UsersApi().getUsers(),
  new ProcessGroupsApi().getProcessGroups(),
  new UsersApi().getUserRoles()
])
  .then(([users, processGroups, roles]) => ({
    users,
    processGroups,
    roles
  }))

export default withPromiseResolver<*, *>(promiseCreator)(UserManagement)
