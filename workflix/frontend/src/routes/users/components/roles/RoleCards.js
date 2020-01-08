// @flow

import React from 'react'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import { sortBy } from 'lodash'
import UserRoleCard from './UserRoleCard'
import CardsContainer from '../CardsContainer'

type PropsType = {|
  users: Map<string, UserType>,
  roles: Map<number, UserRoleType>,
  selection: ?UserRoleType,
  onUserSelected: (UserType) => void
|}

export default class UserCards extends React.Component<PropsType> {
  render () {
    return <CardsContainer title='User roles'>{
      this.getSortedRoles().map<React$Node>(this.getCardForRole)
    }</CardsContainer>
  }

  getSortedRoles (): UserRoleType[] {
    return sortBy(Array.from(this.props.roles.values()), role => role.name)
  }

  getCardForRole = (role: UserRoleType) => {
    const { users, onUserSelected } = this.props
    return <UserRoleCard key={role.id} userRole={role} onUserSelected={onUserSelected} users={users}/>
  }
}
