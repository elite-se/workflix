// @flow

import React from 'react'
import type { UserType } from '../../../../modules/datatypes/User'
import { sortBy } from 'lodash'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import CardsContainer from '../CardsContainer'
import ProcessGroupCard from './ProcessGroupCard'

type PropsType = {|
  users: Map<string, UserType>,
  processGroups: Map<number, ProcessGroupType>,
  selection: ?ProcessGroupType,
  onUserSelected: (UserType) => void,
  onProcessGroupSelected: (ProcessGroupType) => void,
  onGroupMembershipAdded: (ProcessGroupType, UserType) => void,
  onGroupMembershipRemoved: (ProcessGroupType, UserType) => void
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
    const { selection, processGroups, ...cardProps } = this.props
    return <ProcessGroupCard {...cardProps} key={group.id} processGroup={group} selected={selection === group}/>
  }
}
