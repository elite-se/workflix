// @flow

import React from 'react'
import { difference } from 'lodash'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import TitledCard from '../TitledCard'
import IconRow from '../IconRow'
import { Button, ButtonGroup } from '@blueprintjs/core'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'
import { toastifyError } from '../../../../modules/common/toastifyError'
import ProcessGroupsApi from '../../../../modules/api/ProcessGroupsApi'
import SimpleMultiSelect from '../../../../modules/common/components/SimpleMultiSelect'
import UsersApi from '../../../../modules/api/UsersApi'

type PropsType = {|
  user: UserType,
  processGroups: Map<number, ProcessGroupType>,
  roles: Map<number, UserRoleType>,
  onSetEditing: (boolean) => void,
  onGroupMembershipAdded: (ProcessGroupType, UserType) => void,
  onGroupMembershipRemoved: (ProcessGroupType, UserType) => void,
  onRoleMembershipAdded: (UserRoleType, UserType) => void,
  onRoleMembershipRemoved: (UserRoleType, UserType) => void
|}

type StateType = {|
  selectedGroups: ProcessGroupType[],
  selectedRoles: UserRoleType[],
  loading: boolean
|}

class UserCardEdit extends React.Component<PropsType, StateType> {
  state = {
    selectedGroups: this.props.user.processGroupIds.map(id => this.props.processGroups.get(id)).filter(Boolean),
    selectedRoles: this.props.user.userRoleIds.map(id => this.props.roles.get(id)).filter(Boolean),
    loading: false
  }

  onSave = () => {
    this.setState({ loading: true })
    Promise.all([
      this.saveGroups(),
      this.saveRoles()
    ])
      .then(() => {
        this.setState({ loading: false })
        this.props.onSetEditing(false)
      })
      .catch(err => {
        toastifyError(err)
        this.setState({ loading: false })
      })
  }

  saveGroups = () => {
    const { user, processGroups } = this.props
    const newGroups = this.state.selectedGroups.map(group => group.id)
    const oldGroups = user.processGroupIds
    const addedGroups = difference(newGroups, oldGroups)
    const removedGroups = difference(oldGroups, newGroups)
    return Promise.all([
      Promise.all(addedGroups.map(id => processGroups.get(id)).filter(Boolean)
        .map(group => new ProcessGroupsApi().addMembership(group.id, user.id)
          .then(this.props.onGroupMembershipAdded(group, user)))),
      Promise.all(removedGroups.map(id => processGroups.get(id)).filter(Boolean)
        .map(group => new ProcessGroupsApi().removeMembership(group.id, user.id)
          .then(this.props.onGroupMembershipRemoved(group, user))))
    ])
  }

  saveRoles = () => {
    const { user, roles } = this.props
    const newRoles = this.state.selectedRoles.map(role => role.id)
    const oldRoles = user.userRoleIds
    const addedRoles = difference(newRoles, oldRoles)
    const removedRoles = difference(oldRoles, newRoles)
    return Promise.all([
      Promise.all(addedRoles.map(id => roles.get(id)).filter(Boolean)
        .map(role => new UsersApi().addRoleMembership(role.id, user.id)
          .then(this.props.onRoleMembershipAdded(role, user)))),
      Promise.all(removedRoles.map(id => roles.get(id)).filter(Boolean)
        .map(role => new UsersApi().removeRoleMembership(role.id, user.id)
          .then(this.props.onRoleMembershipRemoved(role, user))))
    ])
  }

  onCancel = () => this.props.onSetEditing(false)
  onSelectedGroupsChanged = (selectedGroups: ProcessGroupType[]) => this.setState({ selectedGroups })
  onSelectedRolesChanged = (selectedRoles: UserRoleType[]) => this.setState({ selectedRoles })

  render () {
    const { user, processGroups, roles } = this.props
    const { loading, selectedGroups, selectedRoles } = this.state
    return <TitledCard key={user.id} title={user.name}>
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
      <ButtonGroup fill style={{ marginTop: '5px' }}>
        <Button onClick={this.onSave} icon='floppy-disk' small text='Save' intent={Intent.PRIMARY} loading={loading}/>
        <Button onClick={this.onCancel} icon='undo' small text='Cancel' intent={Intent.DANGER} loading={loading}/>
      </ButtonGroup>
    </TitledCard>
  }

  getGroupId = (group: ProcessGroupType) => group.id
  getGroupTitle = (group: ProcessGroupType) => group.title
  getRoleId = (role: UserRoleType) => role.id
  getRoleName = (role: UserRoleType) => role.name
}

export default UserCardEdit
