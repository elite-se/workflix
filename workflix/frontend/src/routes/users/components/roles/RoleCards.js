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
  onUserSelected: (UserType) => void,
  onRoleSelected: (?UserRoleType) => void,
  onRoleMembershipAdded: (UserRoleType, UserType) => void,
  onRoleMembershipRemoved: (UserRoleType, UserType) => void,
  onRoleChanged: (UserRoleType) => void
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
    const { selection, roles, ...cardProps } = this.props
    return <UserRoleCard {...cardProps} key={role.id} userRole={role} selected={role === selection}/>
  }
}
