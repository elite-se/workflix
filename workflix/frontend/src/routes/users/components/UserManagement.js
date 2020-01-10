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
import { uniq, without } from 'lodash'
import { mapMap } from '../../../modules/common/mapMap'
import { Tab, Tabs } from '@blueprintjs/core'
import styled from 'styled-components'
import UserRoleApi from '../../../modules/api/UserRoleApi'

const CenteredTabs = styled(Tabs)`
  display: flex;
  flex-direction: column;
  align-items: stretch;
  > .bp3-tab-list {
    align-self: center;
  }
  flex-grow: 1;
`

type PropsType = {|
  initialUsers: Map<string, UserType>,
  initialProcessGroups: Map<number, ProcessGroupType>,
  initialRoles: Map<number, UserRoleType>
|}

type ModeType = 'USERS' | 'GROUPS' | 'ROLES'

type StateType = {|
  mode: ModeType,
  users: Map<string, UserType>,
  processGroups: Map<number, ProcessGroupType>,
  roles: Map<number, UserRoleType>,
  selectedUserId: ?string,
  selectedGroupId: ?number,
  selectedRoleId: ?number
|}

class UserManagement extends React.Component<PropsType, StateType> {
  state = {
    mode: 'USERS',
    users: this.props.initialUsers,
    processGroups: this.props.initialProcessGroups,
    roles: this.props.initialRoles,
    selectedUserId: null,
    selectedGroupId: null,
    selectedRoleId: null
  }

  onProcessGroupSelected = (group: ?ProcessGroupType) => {
    this.setState({
      mode: 'GROUPS',
      selectedGroupId: group && group.id
    })
  }

  onRoleSelected = (role: ?UserRoleType) => {
    this.setState({
      mode: 'ROLES',
      selectedRoleId: role && role.id
    })
  }

  onUserSelected = (user: ?UserType) => {
    this.setState({
      mode: 'USERS',
      selectedUserId: user && user.id
    })
  }

  onTabSelected = (newMode: ModeType) => this.setState({
    mode: newMode,
    selectedUserId: null,
    selectedGroupId: null,
    selectedRoleId: null
  })

  onGroupMembershipAdded = (group: ProcessGroupType, user: UserType) => {
    this.setState(oldState => ({
      processGroups: mapMap<number, ProcessGroupType>(oldState.processGroups, (_groupId, _group) =>
        (_groupId !== group.id ? _group : {
          ..._group,
          membersIds: uniq([..._group.membersIds, user.id])
        })),
      users: mapMap<string, UserType>(oldState.users, (_userId, _user) =>
        (_userId !== user.id ? _user : {
          ..._user,
          processGroupIds: uniq([..._user.processGroupIds, group.id])
        }))
    }))
  }

  onGroupMembershipRemoved = (group: ProcessGroupType, user: UserType) => {
    this.setState(oldState => ({
      processGroups: mapMap<number, ProcessGroupType>(oldState.processGroups, (_groupId, _group) =>
        (_groupId !== group.id ? _group : {
          ..._group,
          membersIds: without(_group.membersIds, user.id)
        })),
      users: mapMap<string, UserType>(oldState.users, (_userId, _user) =>
        (_userId !== user.id ? _user : {
          ..._user,
          processGroupIds: without(_user.processGroupIds, group.id)
        }))
    }))
  }

  onRoleMembershipAdded = (role: UserRoleType, user: UserType) => {
    this.setState(oldState => ({
      roles: mapMap<number, UserRoleType>(oldState.roles, (_roleId, _role) =>
        (_roleId !== role.id ? _role : {
          ..._role,
          memberIds: uniq([..._role.memberIds, user.id])
        })),
      users: mapMap<string, UserType>(oldState.users, (_userId, _user) =>
        (_userId !== user.id ? _user : {
          ..._user,
          userRoleIds: uniq([..._user.userRoleIds, role.id])
        }))
    }))
  }

  onRoleMembershipRemoved = (role: UserRoleType, user: UserType) => {
    this.setState(oldState => ({
      roles: mapMap<number, UserRoleType>(oldState.roles, (_roleId, _role) =>
        (_roleId !== role.id ? _role : {
          ..._role,
          memberIds: without(_role.memberIds, user.id)
        })),
      users: mapMap<string, UserType>(oldState.users, (_userId, _user) =>
        (_userId !== user.id ? _user : {
          ..._user,
          userRoleIds: without(_user.userRoleIds, role.id)
        }))
    }))
  }

  onProcessGroupChanged = (processGroup: ProcessGroupType) => {
    this.setState(oldState => ({
      processGroups: mapMap(oldState.processGroups, (id, _group) => id === processGroup.id ? processGroup : _group)
    }))
  }

  onProcessGroupAdded = (processGroup: ProcessGroupType) => {
    this.setState(oldState => ({
      processGroups: new Map(oldState.processGroups).set(processGroup.id, processGroup)
    }))
  }

  onProcessGroupDeleted = (processGroup: ProcessGroupType) => {
    this.setState(oldState => ({
      processGroups: new Map(Array.from(oldState.processGroups.entries()).filter(([id, _]) => id !== processGroup.id))
    }))
  }

  onRoleChanged = (role: UserRoleType) => {
    this.setState(oldState => ({
      roles: mapMap(oldState.roles, (id, _role) => id === role.id ? role : _role)
    }))
  }

  onRoleAdded = (role: UserRoleType) => {
    this.setState(oldState => ({
      roles: new Map(oldState.roles).set(role.id, role)
    }))
  }

  onRoleDeleted = (role: UserRoleType) => {
    this.setState(oldState => ({
      roles: new Map(Array.from(oldState.roles.entries()).filter(([id, _]) => id !== role.id))
    }))
  }

  render () {
    const { processGroups, roles, users, selectedUserId, selectedGroupId, selectedRoleId } = this.state
    const selectedUser = (selectedUserId && users.get(selectedUserId)) || null
    const selectedRole = (selectedRoleId && roles.get(selectedRoleId)) || null
    const selectedGroup = (selectedGroupId && processGroups.get(selectedGroupId)) || null
    const usersPanel = <UserCards users={users} processGroups={processGroups} roles={roles} selection={selectedUser}
                                  onUserSelected={this.onUserSelected}
                                  onRoleSelected={this.onRoleSelected}
                                  onProcessGroupSelected={this.onProcessGroupSelected}
                                  onGroupMembershipAdded={this.onGroupMembershipAdded}
                                  onGroupMembershipRemoved={this.onGroupMembershipRemoved}
                                  onRoleMembershipAdded={this.onRoleMembershipAdded}
                                  onRoleMembershipRemoved={this.onRoleMembershipRemoved}/>
    const rolesPanel = <RoleCards roles={roles} users={users} onUserSelected={this.onUserSelected}
                                  selection={selectedRole}
                                  onRoleSelected={this.onRoleSelected}
                                  onRoleMembershipAdded={this.onRoleMembershipAdded}
                                  onRoleMembershipRemoved={this.onRoleMembershipRemoved}
                                  onRoleChanged={this.onRoleChanged}
                                  onRoleAdded={this.onRoleAdded}
                                  onRoleDeleted={this.onRoleDeleted}/>
    const groupsPanel = <ProcessGroupCards processGroups={processGroups} users={users}
                                           onUserSelected={this.onUserSelected}
                                           selection={selectedGroup}
                                           onProcessGroupSelected={this.onProcessGroupSelected}
                                           onGroupMembershipAdded={this.onGroupMembershipAdded}
                                           onGroupMembershipRemoved={this.onGroupMembershipRemoved}
                                           onProcessGroupChanged={this.onProcessGroupChanged}
                                           onProcessGroupAdded={this.onProcessGroupAdded}
                                           onProcessGroupDeleted={this.onProcessGroupDeleted}/>
    return <CenteredTabs selectedTabId={this.state.mode} onChange={this.onTabSelected} large renderActiveTabPanelOnly>
      <Tab id='USERS' title='Users' panel={usersPanel}/>
      <Tab id='GROUPS' title='Process groups' panel={groupsPanel}/>
      <Tab id='ROLES' title='Roles' panel={rolesPanel}/>
    </CenteredTabs>
  }
}

const promiseCreator = () => Promise.all([
  new UsersApi().getUsers(),
  new ProcessGroupsApi().getProcessGroups(),
  new UserRoleApi().getUserRoles()
])
  .then(([users, processGroups, roles]) => ({
    initialUsers: users,
    initialProcessGroups: processGroups,
    initialRoles: roles
  }))

export default withPromiseResolver<*, *>(promiseCreator)(UserManagement)
