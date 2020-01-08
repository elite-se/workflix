// @flow

import React from 'react'
import { difference, sortBy } from 'lodash'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import TitledCard from '../TitledCard'
import IconRow from '../IconRow'
import listIfNeeded from '../../listIfNeeded'
import { Button, ButtonGroup } from '@blueprintjs/core'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'
import ProcessGroupMultiSelect from '../../../../modules/common/components/ProcessGroupMultiselect'
import { toastifyError } from '../../../../modules/common/toastifyError'
import ProcessGroupsApi from '../../../../modules/api/ProcessGroupsApi'

type PropsType = {|
  user: UserType,
  processGroups: Map<number, ProcessGroupType>,
  roles: Map<number, UserRoleType>,
  onSetEditing: (boolean) => void,
  onGroupMembershipAdded: (ProcessGroupType, UserType) => void,
  onGroupMembershipRemoved: (ProcessGroupType, UserType) => void
|}

type StateType = {|
  selectedGroups: ProcessGroupType[],
  loading: boolean
|}

class UserCardEdit extends React.Component<PropsType, StateType> {
  state = {
    selectedGroups: this.props.user.processGroupIds.map(id => this.props.processGroups.get(id)).filter(Boolean),
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
      .catch(toastifyError)
  }

  saveGroups = () => {
    const { user, processGroups } = this.props
    const newGroups = this.state.selectedGroups.map(group => group.id)
    const oldGroups = user.processGroupIds
    const addedGroups = difference(newGroups, oldGroups)
    const removedGroups = difference(oldGroups, newGroups)
    return Promise.all([
      addedGroups.map(id => processGroups.get(id)).filter(Boolean)
        .map(group => new ProcessGroupsApi().addMembership(group.id, user.id)
          .then(this.props.onGroupMembershipAdded(group, user))),
      removedGroups.map(id => processGroups.get(id)).filter(Boolean)
        .map(group => new ProcessGroupsApi().removeMembership(group.id, user.id)
          .then(this.props.onGroupMembershipRemoved(group, user)))
    ])
  }

  saveRoles = () => {
    // TODO implement
    return Promise.resolve(null)
  }

  onCancel = () => this.props.onSetEditing(false)
  onSelectedGroupsChanged = (selectedGroups: ProcessGroupType[]) => this.setState({ selectedGroups })

  render () {
    const { user, processGroups, roles } = this.props
    const { loading, selectedGroups } = this.state
    const usersRoles = sortBy(user.userRoleIds.map(id => roles.get(id)).filter(Boolean),
      role => role.name)
    return <TitledCard key={user.id} title={user.name}>
        <IconRow icon='person'>{user.displayname}</IconRow>
        <IconRow icon='envelope'><a href={`mailto:${user.email}`}>{user.email}</a></IconRow>
        <IconRow icon='office'>
          <ProcessGroupMultiSelect allGroups={processGroups} selectedGroups={selectedGroups}
                                   onSelectionChanged={this.onSelectedGroupsChanged} fill/>
        </IconRow>
        <IconRow icon='badge'>
          {listIfNeeded(usersRoles, role => role.id,
            role => role.name)}
        </IconRow>
      <ButtonGroup fill style={{ marginTop: '5px' }}>
        <Button onClick={this.onSave} icon='floppy-disk' small text='Save' intent={Intent.PRIMARY} loading={loading}/>
        <Button onClick={this.onCancel} icon='undo' small text='Cancel' intent={Intent.DANGER} loading={loading}/>
      </ButtonGroup>
    </TitledCard>
  }
}

export default UserCardEdit
