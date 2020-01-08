// @flow

import React from 'react'
import { difference } from 'lodash'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import TitledCard from '../TitledCard'
import IconRow from '../IconRow'
import { toastifyError } from '../../../../modules/common/toastifyError'
import ProcessGroupsApi from '../../../../modules/api/ProcessGroupsApi'
import SimpleMultiSelect from '../../../../modules/common/components/SimpleMultiSelect'
import UsersApi from '../../../../modules/api/UsersApi'
import { Elevation } from '@blueprintjs/core/lib/cjs/common/elevation'
import ScrollIntoViewOnMount from '../../../../modules/common/components/ScrollIntoViewOnMount'

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
  onSelectedGroupsChanged = (selectedGroups: ProcessGroupType[]) => {
    const { user, processGroups } = this.props
    const newGroups = selectedGroups.map(group => group.id)
    const oldGroups = user.processGroupIds
    const addedGroups = difference(newGroups, oldGroups)
    const removedGroups = difference(oldGroups, newGroups)
    addedGroups.map(id => processGroups.get(id)).filter(Boolean)
      .forEach(group => new ProcessGroupsApi().addMembership(group.id, user.id)
        .then(this.props.onGroupMembershipAdded(group, user))
        .catch(toastifyError))
    removedGroups.map(id => processGroups.get(id)).filter(Boolean)
      .forEach(group => new ProcessGroupsApi().removeMembership(group.id, user.id)
        .then(this.props.onGroupMembershipRemoved(group, user))
        .catch(toastifyError))
  }

  onSelectedRolesChanged = (selectedRoles: UserRoleType[]) => {
    const { user, roles } = this.props
    const newRoles = selectedRoles.map(role => role.id)
    const oldRoles = user.userRoleIds
    const addedRoles = difference(newRoles, oldRoles)
    const removedRoles = difference(oldRoles, newRoles)
    addedRoles.map(id => roles.get(id)).filter(Boolean)
      .forEach(role => new UsersApi().addRoleMembership(role.id, user.id)
        .then(this.props.onRoleMembershipAdded(role, user))
        .catch(toastifyError))
    removedRoles.map(id => roles.get(id)).filter(Boolean)
      .forEach(role => new UsersApi().removeRoleMembership(role.id, user.id)
        .then(this.props.onRoleMembershipRemoved(role, user))
        .catch(toastifyError))
  }

  render () {
    const { user, processGroups, roles } = this.props
    const selectedGroups = this.props.user.processGroupIds.map(id => this.props.processGroups.get(id)).filter(Boolean)
    const selectedRoles = this.props.user.userRoleIds.map(id => this.props.roles.get(id)).filter(Boolean)
    return <ScrollIntoViewOnMount><TitledCard key={user.id} title={user.name} elevation={Elevation.FOUR}>
      <IconRow icon='person'>{user.displayname}</IconRow>
      <IconRow icon='envelope'><a href={`mailto:${user.email}`}>{user.email}</a></IconRow>
      <IconRow icon='office'>
        <SimpleMultiSelect items={Array.from(processGroups.values())} selection={selectedGroups}
                           onSelectionChanged={this.onSelectedGroupsChanged} multiSelectProps={{ fill: true }}
                           toID={this.getGroupId} render={this.getGroupTitle}/>
      </IconRow>
      <IconRow icon='badge'>
        <SimpleMultiSelect items={Array.from(roles.values())} selection={selectedRoles}
                           onSelectionChanged={this.onSelectedRolesChanged} multiSelectProps={{ fill: true }}
                           toID={this.getRoleId} render={this.getRoleName}/>
      </IconRow>
    </TitledCard></ScrollIntoViewOnMount>
  }

  getGroupId = (group: ProcessGroupType) => group.id
  getGroupTitle = (group: ProcessGroupType) => group.title
  getRoleId = (role: UserRoleType) => role.id
  getRoleName = (role: UserRoleType) => role.name
}

export default UserCardEdit
