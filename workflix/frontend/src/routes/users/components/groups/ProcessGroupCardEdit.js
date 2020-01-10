// @flow

import React from 'react'
import type { UserType } from '../../../../modules/datatypes/User'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import IconRow from '../IconRow'
import TitledCard from '../StyledCard'
import SimpleMultiSelect from '../../../../modules/common/components/SimpleMultiSelect'
import { toastifyError } from '../../../../modules/common/toastifyError'
import ProcessGroupsApi from '../../../../modules/api/ProcessGroupsApi'
import { EditableText, Elevation, H3 } from '@blueprintjs/core'

type PropsType = {|
  processGroup: ProcessGroupType,
  users: Map<string, UserType>,
  onGroupMembershipAdded: (ProcessGroupType, UserType) => void,
  onGroupMembershipRemoved: (ProcessGroupType, UserType) => void,
  onProcessGroupChanged: (ProcessGroupType) => void
|}

class ProcessGroupCardEdit extends React.Component<PropsType> {
  onUserAdded = (user: UserType) => {
    const { processGroup } = this.props
    new ProcessGroupsApi().addMembership(processGroup.id, user.id)
      .then(this.props.onGroupMembershipAdded(processGroup, user))
      .catch(toastifyError)
  }

  onUserRemoved = (user: UserType) => {
    const { processGroup } = this.props
    new ProcessGroupsApi().removeMembership(processGroup.id, user.id)
      .then(this.props.onGroupMembershipRemoved(processGroup, user))
      .catch(toastifyError)
  }

  onUsersCleared = () => {
    this.getSelectedUsers().forEach(user => this.onUserRemoved(user))
  }

  patchAndPropagate = (updatedGroup: ProcessGroupType) => {
    new ProcessGroupsApi().patchProcessGroup(updatedGroup)
      .then(this.props.onProcessGroupChanged(updatedGroup))
      .catch(toastifyError)
  }

  onTitleChanged = (title: string) => {
    this.patchAndPropagate({
      ...this.props.processGroup,
      title
    })
  }

  onDescriptionChanged = (description: string) => {
    this.patchAndPropagate({
      ...this.props.processGroup,
      description
    })
  }

  getSelectedUsers = () => this.props.processGroup.membersIds.map(id => this.props.users.get(id)).filter(Boolean)

  render () {
    const { processGroup, users } = this.props
    return <TitledCard key={processGroup.id} elevation={Elevation.FOUR} interactive>
      <IconRow icon='office'><H3>
        <EditableText onConfirm={this.onTitleChanged} defaultValue={processGroup.title} placeholder='Title'
                      alwaysRenderInput/>
      </H3></IconRow>
      <IconRow icon='annotation' multiLine>
        <EditableText onConfirm={this.onDescriptionChanged} defaultValue={processGroup.description}
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

export default ProcessGroupCardEdit
