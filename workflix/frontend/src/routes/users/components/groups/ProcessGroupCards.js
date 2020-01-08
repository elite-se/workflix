// @flow

import React from 'react'
import type { UserType } from '../../../../modules/datatypes/User'
import { sortBy } from 'lodash'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import ProcessGroupCard from './ProcessGroupCard'
import CardsContainer from '../CardsContainer'

type PropsType = {|
  users: Map<string, UserType>,
  processGroups: Map<number, ProcessGroupType>,
  selection: ?ProcessGroupType,
  onUserSelected: (UserType) => void
|}

export default class ProcessGroupCards extends React.Component<PropsType> {
  render () {
    return <CardsContainer title='Process groups'>{
      this.getSortedGroups().map<React$Node>(this.getCardForGroup)
    }</CardsContainer>
  }

  getSortedGroups (): ProcessGroupType[] {
    return sortBy(Array.from(this.props.processGroups.values()), group => group.title)
  }

  getCardForGroup = (group: ProcessGroupType) => {
    const { users, onUserSelected } = this.props
    return <ProcessGroupCard key={group.id} processGroup={group} onUserSelected={onUserSelected} users={users}/>
  }
}
