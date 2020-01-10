// @flow

import React from 'react'
import type { UserType } from '../../../../modules/datatypes/User'
import { sortBy } from 'lodash'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import CardsContainer from '../CardsContainer'
import ProcessGroupCard from './ProcessGroupCard'
import { getCurrentUserId } from '../../../../modules/common/tokenStorage'
import ProcessGroupsApi from '../../../../modules/api/ProcessGroupsApi'
import { toastifyError } from '../../../../modules/common/toastifyError'

type PropsType = {|
  users: Map<string, UserType>,
  processGroups: Map<number, ProcessGroupType>,
  selection: ?ProcessGroupType,
  onUserSelected: (UserType) => void,
  onProcessGroupSelected: (?ProcessGroupType) => void,
  onGroupMembershipAdded: (ProcessGroupType, UserType) => void,
  onGroupMembershipRemoved: (ProcessGroupType, UserType) => void,
  onProcessGroupChanged: (ProcessGroupType) => void,
  onProcessGroupAdded: (ProcessGroupType) => void,
  onProcessGroupDeleted: (ProcessGroupType) => void
|}

export default class ProcessGroupCards extends React.Component<PropsType> {
  onCreate = () => {
    this.props.onProcessGroupSelected(null)
    const newGroupSkeleton = {
      title: 'New process group',
      description: '',
      ownerId: getCurrentUserId(),
      createdAt: new Date(),
      membersIds: []
    }
    return new ProcessGroupsApi().addProcessGroup(newGroupSkeleton)
      .then(({ newId }) => {
        const newGroup = {
          ...newGroupSkeleton,
          id: newId
        }
        this.props.onProcessGroupAdded(newGroup)
        this.props.onProcessGroupSelected(newGroup)
      })
      .catch(toastifyError)
  }

  render () {
    return <CardsContainer onCreate={this.onCreate}>{
      this.getSortedGroups().map<React$Node>(this.getCardForGroup)
    }</CardsContainer>
  }

  getSortedGroups (): ProcessGroupType[] {
    return sortBy(Array.from(this.props.processGroups.values()), group => group.title)
  }

  getCardForGroup = (group: ProcessGroupType) => {
    const { selection, processGroups, onProcessGroupAdded, ...cardProps } = this.props
    return <ProcessGroupCard {...cardProps} key={group.id} processGroup={group} selected={selection === group}/>
  }
}
