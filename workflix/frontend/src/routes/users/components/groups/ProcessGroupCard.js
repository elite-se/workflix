// @flow

import React from 'react'
import type { UserType } from '../../../../modules/datatypes/User'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import ProcessGroupCardRead from './ProcessGroupCardRead'
import ProcessGroupCardEdit from './ProcessGroupCardEdit'
import OutsideClickHandler from 'react-outside-click-handler/esm/OutsideClickHandler'
import ScrollIntoViewOnMount from '../../../../modules/common/components/ScrollIntoViewOnMount'

type PropsType = {|
  processGroup: ProcessGroupType,
  users: Map<string, UserType>,
  selected: boolean,
  onUserSelected: (UserType) => void,
  onProcessGroupSelected: (ProcessGroupType) => void,
  onGroupMembershipAdded: (ProcessGroupType, UserType) => void,
  onGroupMembershipRemoved: (ProcessGroupType, UserType) => void,
  onProcessGroupChanged: (ProcessGroupType) => void
|}

class ProcessGroupCard extends React.Component<PropsType> {
  render () {
    const {
      processGroup, users, onUserSelected, selected, onProcessGroupSelected, onGroupMembershipAdded,
      onGroupMembershipRemoved, onProcessGroupChanged
    } = this.props
    return selected
      ? <OutsideClickHandler><ScrollIntoViewOnMount>
        <ProcessGroupCardEdit processGroup={processGroup} users={users}
                              onGroupMembershipAdded={onGroupMembershipAdded}
                              onGroupMembershipRemoved={onGroupMembershipRemoved}
                              onProcessGroupChanged={onProcessGroupChanged}/>
      </ScrollIntoViewOnMount></OutsideClickHandler>
      : <ProcessGroupCardRead processGroup={processGroup} users={users} onUserSelected={onUserSelected}
                              onProcessGroupSelected={onProcessGroupSelected}/>
  }
}

export default ProcessGroupCard
