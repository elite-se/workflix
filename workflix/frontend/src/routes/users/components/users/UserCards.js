// @flow

import React from 'react'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import { sortBy } from 'lodash'
import UserCard from './UserCard'
import CardsContainer from '../CardsContainer'

type PropsType = {|
  users: Map<string, UserType>,
  processGroups: Map<number, ProcessGroupType>,
  roles: Map<number, UserRoleType>,
  selection: ?UserType,
  onRoleSelected: (?UserRoleType) => void,
  onUserSelected: (?UserType) => void,
  onProcessGroupSelected: (?ProcessGroupType) => void,
  onGroupMembershipAdded: (ProcessGroupType, UserType) => void,
  onGroupMembershipRemoved: (ProcessGroupType, UserType) => void,
  onRoleMembershipAdded: (UserRoleType, UserType) => void,
  onRoleMembershipRemoved: (UserRoleType, UserType) => void
|}

export default class UserCards extends React.Component<PropsType> {
  render () {
    return <CardsContainer title='Users'>{this.getSortedUsers().map<React$Node>(this.getCardForUser)}</CardsContainer>
  }

  getSortedUsers (): UserType[] {
    return sortBy(Array.from(this.props.users.values()), user => user.name)
  }

  getCardForUser = (user: UserType) => {
    const { users, selection, ...childProps } = this.props
    return <UserCard {...childProps} key={user.id} user={user} selected={selection === user}/>
  }
}
