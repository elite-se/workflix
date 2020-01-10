// @flow

import React from 'react'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import IconRow from '../IconRow'
import StyledCard from '../StyledCard'
import SimpleMultiSelect from '../../../../modules/common/components/SimpleMultiSelect'
import { toastifyError } from '../../../../modules/common/toastifyError'
import { Button, EditableText, Elevation, H3 } from '@blueprintjs/core'
import UsersApi from '../../../../modules/api/UsersApi'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'

type PropsType = {|
  userRole: UserRoleType,
  users: Map<string, UserType>,
  onRoleMembershipAdded: (UserRoleType, UserType) => void,
  onRoleMembershipRemoved: (UserRoleType, UserType) => void,
  onRoleChanged: (UserRoleType) => void,
  onRoleDeleted: (UserRoleType) => void
|}

class UserRoleCardEdit extends React.Component<PropsType, { deleting: boolean }> {
  state = { deleting: false }

  onUserAdded = (user: UserType) => {
    const { userRole } = this.props
    new UsersApi().addRoleMembership(userRole.id, user.id)
      .then(this.props.onRoleMembershipAdded(userRole, user))
      .catch(toastifyError)
  }

  onUserRemoved = (user: UserType) => {
    const { userRole } = this.props
    new UsersApi().removeRoleMembership(userRole.id, user.id)
      .then(this.props.onRoleMembershipRemoved(userRole, user))
      .catch(toastifyError)
  }

  onUsersCleared = () => {
    this.getSelectedUsers().forEach(user => this.onUserRemoved(user))
  }

  patchAndPropagate = (updatedRole: UserRoleType) => {
    new UsersApi().patchUserRole(updatedRole)
      .then(this.props.onRoleChanged(updatedRole))
      .catch(toastifyError)
  }

  onNameChanged = (name: string) => {
    this.patchAndPropagate({
      ...this.props.userRole,
      name
    })
  }

  onDescriptionChanged = (description: string) => {
    this.patchAndPropagate({
      ...this.props.userRole,
      description
    })
  }

  onDelete = () => {
    this.setState({ deleting: true })
    new UsersApi().deleteUserRole(this.props.userRole.id)
      .then(() => {
        this.setState({ deleting: false })
        this.props.onRoleDeleted(this.props.userRole)
      })
      .catch(err => {
        this.setState({ deleting: false })
        toastifyError(err)
      })
  }

  getSelectedUsers = () => this.props.userRole.memberIds.map(id => this.props.users.get(id)).filter(Boolean)

  render () {
    const { userRole, users } = this.props
    return <StyledCard key={userRole.id} elevation={Elevation.FOUR} interactive>
      <IconRow icon='people'><H3>
        <EditableText onConfirm={this.onNameChanged} defaultValue={userRole.name} placeholder='Name'
                      alwaysRenderInput/>
      </H3></IconRow>
      <IconRow icon='annotation' multiLine>
        <EditableText onConfirm={this.onDescriptionChanged} defaultValue={userRole.description}
                      placeholder='Description' multiline/>
      </IconRow>
      <IconRow icon='person' multiLine>
        <SimpleMultiSelect items={Array.from(users.values())} selection={this.getSelectedUsers()}
                           multiSelectProps={{
                             fill: true,
                             popoverProps: { usePortal: false }
                           }}
                           toID={this.getUserId} render={this.getUserName}
                           onItemAdded={this.onUserAdded} onItemRemoved={this.onUserRemoved}
                           onItemsCleared={this.onUsersCleared}/>
      </IconRow>
      <Button icon='trash' intent={Intent.DANGER} style={{ marginTop: 'auto' }} onClick={this.onDelete}
              loading={this.state.deleting} fill small text='Delete'/>
    </StyledCard>
  }

  getUserId = (user: UserType) => user.id
  getUserName = (user: UserType) => user.name
}

export default UserRoleCardEdit
