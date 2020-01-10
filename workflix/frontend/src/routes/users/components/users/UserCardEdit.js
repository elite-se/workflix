// @flow

import React from 'react'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import StyledCard from '../StyledCard'
import IconRow from '../IconRow'
import { toastifyError } from '../../../../modules/common/toastifyError'
import ProcessGroupsApi from '../../../../modules/api/ProcessGroupsApi'
import SimpleMultiSelect from '../../../../modules/common/components/SimpleMultiSelect'
import UsersApi from '../../../../modules/api/UsersApi'
import { Elevation, H3 } from '@blueprintjs/core'

type PropsType = {|
  user: UserType,
  processGroups: Map<number, ProcessGroupType>,
  roles: Map<number, UserRoleType>,
  onGroupMembershipAdded: (ProcessGroupType, UserType) => void,
  onGroupMembershipRemoved: (ProcessGroupType, UserType) => void,
  onRoleMembershipAdded: (UserRoleType, UserType) => void,
  onRoleMembershipRemoved: (UserRoleType, UserType) => void
|}

class UserCardEdit extends React.Component<PropsType> {
  onGroupAdded = (group: ProcessGroupType) => {
    const { user } = this.props
    new ProcessGroupsApi().addMembership(group.id, user.id)
      .then(this.props.onGroupMembershipAdded(group, user))
      .catch(toastifyError)
  }

  onGroupRemoved = (group: ProcessGroupType) => {
    const { user } = this.props
    new ProcessGroupsApi().removeMembership(group.id, user.id)
      .then(this.props.onGroupMembershipRemoved(group, user))
      .catch(toastifyError)
  }

  onGroupsCleared = () => this.getSelectedGroups().forEach(group => this.onGroupRemoved(group))

  onRoleAdded = (role: UserRoleType) => {
    const { user } = this.props
    new UsersApi().addRoleMembership(role.id, user.id)
      .then(this.props.onRoleMembershipAdded(role, user))
      .catch(toastifyError)
  }

  onRoleRemoved = (role: UserRoleType) => {
    const { user } = this.props
    new UsersApi().removeRoleMembership(role.id, user.id)
      .then(this.props.onRoleMembershipRemoved(role, user))
      .catch(toastifyError)
  }

  onRolesCleared = () => this.getSelectedRoles().forEach(role => this.onRoleRemoved(role))

  getSelectedGroups = () => this.props.user.processGroupIds.map(id => this.props.processGroups.get(id)).filter(Boolean)
  getSelectedRoles = () => this.props.user.userRoleIds.map(id => this.props.roles.get(id)).filter(Boolean)

  render () {
    const { user, processGroups, roles } = this.props
    return <StyledCard key={user.id} elevation={Elevation.FOUR} interactive>
      <H3>{user.name}</H3>
      <IconRow icon='person'>{user.displayname}</IconRow>
      <IconRow icon='envelope'><a href={`mailto:${user.email}`}>{user.email}</a></IconRow>
      <IconRow icon='office'>
        <SimpleMultiSelect items={Array.from(processGroups.values())} selection={this.getSelectedGroups()}
                           multiSelectProps={{
                             fill: true,
                             popoverProps: { usePortal: false }
                           }}
                           toID={this.getGroupId} render={this.getGroupTitle}
                           onItemAdded={this.onGroupAdded} onItemRemoved={this.onGroupRemoved}
                           onItemsCleared={this.onGroupsCleared}/>
      </IconRow>
      <IconRow icon='badge'>
        <SimpleMultiSelect items={Array.from(roles.values())} selection={this.getSelectedRoles()}
                           multiSelectProps={{
                             fill: true,
                             popoverProps: { usePortal: false }
                           }}
                           toID={this.getRoleId} render={this.getRoleName}
                           onItemAdded={this.onRoleAdded} onItemRemoved={this.onRoleRemoved}
                           onItemsCleared={this.onRolesCleared}/>
      </IconRow>
    </StyledCard>
  }

  getGroupId = (group: ProcessGroupType) => group.id
  getGroupTitle = (group: ProcessGroupType) => group.title
  getRoleId = (role: UserRoleType) => role.id
  getRoleName = (role: UserRoleType) => role.name
}

export default UserCardEdit
