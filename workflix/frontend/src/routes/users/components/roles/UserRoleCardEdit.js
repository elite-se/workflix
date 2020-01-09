// @flow

import React from 'react'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import IconRow from '../IconRow'
import TitledCard from '../TitledCard'
import SimpleMultiSelect from '../../../../modules/common/components/SimpleMultiSelect'
import { toastifyError } from '../../../../modules/common/toastifyError'
import { EditableText, Elevation } from '@blueprintjs/core'
import UsersApi from '../../../../modules/api/UsersApi'

type PropsType = {|
  userRole: UserRoleType,
  users: Map<string, UserType>,
  onRoleMembershipAdded: (UserRoleType, UserType) => void,
  onRoleMembershipRemoved: (UserRoleType, UserType) => void,
  onRoleChanged: (UserRoleType) => void
|}

class UserRoleCardEdit extends React.Component<PropsType> {
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
    new UsersApi().patchRole(updatedRole)
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

  getSelectedUsers = () => this.props.userRole.memberIds.map(id => this.props.users.get(id)).filter(Boolean)

  render () {
    const { userRole, users } = this.props
    return <TitledCard key={userRole.id} elevation={Elevation.FOUR} title={
      <IconRow icon='people'>
        <EditableText onConfirm={this.onNameChanged} defaultValue={userRole.name} placeholder='Name'
                      alwaysRenderInput/>
      </IconRow>
    }>
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
    </TitledCard>
  }

  getUserId = (user: UserType) => user.id
  getUserName = (user: UserType) => user.name
}

export default UserRoleCardEdit
