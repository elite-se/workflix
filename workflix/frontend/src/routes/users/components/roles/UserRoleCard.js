// @flow

import React from 'react'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import UserRoleCardRead from './UserRoleCardRead'
import UserRoleCardEdit from './UserRoleCardEdit'
import EditCardWrapper from '../EditCardWrapper'

type PropsType = {|
  userRole: UserRoleType,
  users: Map<string, UserType>,
  selected: boolean,
  onUserSelected: (UserType) => void,
  onRoleSelected: (?UserRoleType) => void,
  onRoleMembershipAdded: (UserRoleType, UserType) => void,
  onRoleMembershipRemoved: (UserRoleType, UserType) => void,
  onRoleChanged: (UserRoleType) => void
|}

class UserRoleCard extends React.Component<PropsType> {
  onDeselection = () => this.props.onRoleSelected(null)

  render () {
    const {
      userRole, users, onUserSelected, onRoleSelected, onRoleMembershipRemoved, onRoleMembershipAdded, onRoleChanged,
      selected
    } = this.props
    return selected
      ? <EditCardWrapper onDeselect={this.onDeselection}>
        <UserRoleCardEdit onRoleMembershipRemoved={onRoleMembershipRemoved} users={users} userRole={userRole}
                          onRoleMembershipAdded={onRoleMembershipAdded} onRoleChanged={onRoleChanged}/>
      </EditCardWrapper>
      : <UserRoleCardRead userRole={userRole} users={users} onUserSelected={onUserSelected}
                          onRoleSelected={onRoleSelected}/>
  }
}

export default UserRoleCard
