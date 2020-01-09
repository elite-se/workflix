// @flow

import React from 'react'
import type { UserType } from '../../../../modules/datatypes/User'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import { sortBy } from 'lodash'
import IconRow from '../IconRow'
import listIfNeeded from '../../listIfNeeded'
import TitledCard from '../TitledCard'
import stopPropagation from '../../../../modules/common/stopPropagation'

type PropsType = {|
  processGroup: ProcessGroupType,
  users: Map<string, UserType>,
  onUserSelected: (UserType) => void,
  onProcessGroupSelected: (ProcessGroupType) => void
|}

class ProcessGroupCardRead extends React.Component<PropsType> {
  onUserSelected = (user: UserType) => stopPropagation(() => this.props.onUserSelected(user))
  onClick = () => this.props.onProcessGroupSelected(this.props.processGroup)

  render () {
    const { processGroup, users } = this.props
    const groupUsers = sortBy(processGroup.membersIds.map(id => users.get(id)).filter(Boolean),
      user => user.name)
    return <TitledCard key={processGroup.id} title={<IconRow icon='office'>{processGroup.title}</IconRow>}
                       onClick={this.onClick}>
      <IconRow icon='annotation' multiLine>
        <span style={{ whiteSpace: 'pre-wrap' }}>{processGroup.description}</span>
      </IconRow>
      <IconRow icon='person' multiLine>
        {listIfNeeded(groupUsers, user => user.id,
          user => <a onClick={this.onUserSelected(user)}>{user.name}</a>)}
      </IconRow>
    </TitledCard>
  }
}

export default ProcessGroupCardRead
