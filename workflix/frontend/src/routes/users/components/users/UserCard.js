// @flow

import React from 'react'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import UserCardRead from './UserCardRead'
import UserCardEdit from './UserCardEdit'

type PropsType = {|
  user: UserType,
  processGroups: Map<number, ProcessGroupType>,
  roles: Map<number, UserRoleType>,
  onRoleSelected: (UserRoleType) => void,
  onProcessGroupSelected: (ProcessGroupType) => void,
  onGroupMembershipAdded: (ProcessGroupType, UserType) => void,
  onGroupMembershipRemoved: (ProcessGroupType, UserType) => void,
  onRoleMembershipAdded: (UserRoleType, UserType) => void,
  onRoleMembershipRemoved: (UserRoleType, UserType) => void
|}

type StateType = {|
  editing: boolean
|}

class UserCard extends React.Component<PropsType, StateType> {
  state = { editing: false }

  onSetEditing = (editing: boolean) => this.setState({ editing })

  render () {
    const {
      user, processGroups, roles, onRoleSelected, onProcessGroupSelected, onGroupMembershipAdded,
      onGroupMembershipRemoved, onRoleMembershipAdded, onRoleMembershipRemoved
    } = this.props
    return this.state.editing
      ? <UserCardEdit user={user} processGroups={processGroups} roles={roles} onSetEditing={this.onSetEditing}
      onGroupMembershipAdded={onGroupMembershipAdded} onGroupMembershipRemoved={onGroupMembershipRemoved}
      onRoleMembershipAdded={onRoleMembershipAdded} onRoleMembershipRemoved={onRoleMembershipRemoved}/>
      : <UserCardRead user={user} processGroups={processGroups} roles={roles} onSetEditing={this.onSetEditing}
                    onRoleSelected={onRoleSelected} onProcessGroupSelected={onProcessGroupSelected}/>
  }
}

export default UserCard
