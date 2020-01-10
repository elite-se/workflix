// @flow

import React from 'react'
import type { UserType } from '../../../../modules/datatypes/User'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import ProcessGroupCardRead from './ProcessGroupCardRead'
import ProcessGroupCardEdit from './ProcessGroupCardEdit'
import EditCardWrapper from '../EditCardWrapper'

type PropsType = {|
  processGroup: ProcessGroupType,
  users: Map<string, UserType>,
  selected: boolean,
  onUserSelected: (UserType) => void,
  onProcessGroupSelected: (?ProcessGroupType) => void,
  onGroupMembershipAdded: (ProcessGroupType, UserType) => void,
  onGroupMembershipRemoved: (ProcessGroupType, UserType) => void,
  onProcessGroupChanged: (ProcessGroupType) => void
|}

class ProcessGroupCard extends React.Component<PropsType> {
  onDeselection = () => this.props.onProcessGroupSelected(null)

  render () {
    const {
      processGroup, users, onUserSelected, selected, onProcessGroupSelected, onGroupMembershipAdded,
      onGroupMembershipRemoved, onProcessGroupChanged
    } = this.props
    return selected
      ? <EditCardWrapper onDeselect={this.onDeselection}>
        <ProcessGroupCardEdit processGroup={processGroup} users={users}
                              onGroupMembershipAdded={onGroupMembershipAdded}
                              onGroupMembershipRemoved={onGroupMembershipRemoved}
                              onProcessGroupChanged={onProcessGroupChanged}/>
      </EditCardWrapper>
      : <ProcessGroupCardRead processGroup={processGroup} users={users} onUserSelected={onUserSelected}
                              onProcessGroupSelected={onProcessGroupSelected}/>
  }
}

export default ProcessGroupCard
