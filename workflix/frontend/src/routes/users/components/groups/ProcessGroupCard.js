// @flow

import React from 'react'
import type { UserType } from '../../../../modules/datatypes/User'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import { sortBy } from 'lodash'
import IconRow from '../IconRow'
import listIfNeeded from '../../listIfNeeded'
import TitledCard from '../TitledCard'

type PropsType = {|
  processGroup: ProcessGroupType,
  users: Map<string, UserType>,
  onUserSelected: (UserType) => void
|}

class ProcessGroupCard extends React.Component<PropsType> {
  onUserSelected = (user: UserType) => () => this.props.onUserSelected(user)

  render () {
    const { processGroup, users } = this.props
    const groupUsers = sortBy(processGroup.membersIds.map(id => users.get(id)).filter(Boolean),
      user => user.name)
    return <TitledCard key={processGroup.id} title={<IconRow icon='office' singleLine>{processGroup.title}</IconRow>}>
      <IconRow icon='person'>
        {listIfNeeded(groupUsers, user => user.id,
          user => <a onClick={this.onUserSelected(user)}>{user.name}</a>)}
      </IconRow>
    </TitledCard>
  }
}

export default ProcessGroupCard
