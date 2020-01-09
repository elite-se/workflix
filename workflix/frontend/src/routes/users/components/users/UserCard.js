// @flow

import React from 'react'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import UserCardRead from './UserCardRead'
import UserCardEdit from './UserCardEdit'
import ScrollIntoViewOnMount from '../../../../modules/common/components/ScrollIntoViewOnMount'
import OutsideClickHandler from 'react-outside-click-handler'

type PropsType = {|
  user: UserType,
  processGroups: Map<number, ProcessGroupType>,
  roles: Map<number, UserRoleType>,
  selected: boolean,
  onUserSelected: (?UserType) => void,
  onRoleSelected: (?UserRoleType) => void,
  onProcessGroupSelected: (?ProcessGroupType) => void,
  onGroupMembershipAdded: (ProcessGroupType, UserType) => void,
  onGroupMembershipRemoved: (ProcessGroupType, UserType) => void,
  onRoleMembershipAdded: (UserRoleType, UserType) => void,
  onRoleMembershipRemoved: (UserRoleType, UserType) => void
|}

class UserCard extends React.Component<PropsType> {
  onDeselection = () => {
    this.props.onUserSelected(null)
  }

  render () {
    const {
      user, processGroups, roles, onRoleSelected, onProcessGroupSelected, onGroupMembershipAdded,
      onGroupMembershipRemoved, onRoleMembershipAdded, onRoleMembershipRemoved, selected, onUserSelected
    } = this.props
    return selected
      ? <OutsideClickHandler onOutsideClick={this.onDeselection}><ScrollIntoViewOnMount>
        <UserCardEdit user={user} processGroups={processGroups} roles={roles}
                      onGroupMembershipAdded={onGroupMembershipAdded}
                      onGroupMembershipRemoved={onGroupMembershipRemoved}
                      onRoleMembershipAdded={onRoleMembershipAdded} onRoleMembershipRemoved={onRoleMembershipRemoved}/>
      </ScrollIntoViewOnMount></OutsideClickHandler>
      : <UserCardRead user={user} processGroups={processGroups} roles={roles} onUserSelected={onUserSelected}
                      onRoleSelected={onRoleSelected} onProcessGroupSelected={onProcessGroupSelected}/>
  }
}

export default UserCard
