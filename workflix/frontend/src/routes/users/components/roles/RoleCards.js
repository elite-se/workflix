// @flow

import React from 'react'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import { sortBy } from 'lodash'
import UserRoleCard from './UserRoleCard'
import CardsContainer from '../CardsContainer'
import { toastifyError } from '../../../../modules/common/toastifyError'
import UserRoleApi from '../../../../modules/api/UserRoleApi'

type PropsType = {|
  users: Map<string, UserType>,
  roles: Map<number, UserRoleType>,
  selection: ?UserRoleType,
  onUserSelected: (UserType) => void,
  onRoleSelected: (?UserRoleType) => void,
  onRoleMembershipAdded: (UserRoleType, UserType) => void,
  onRoleMembershipRemoved: (UserRoleType, UserType) => void,
  onRoleChanged: (UserRoleType) => void,
  onRoleAdded: (UserRoleType) => void,
  onRoleDeleted: (UserRoleType) => void
|}

export default class UserCards extends React.Component<PropsType> {
  onCreate = () => {
    this.props.onRoleSelected(null)
    const newRoleSkeleton = {
      name: 'New role',
      description: '',
      createdAt: new Date(),
      memberIds: []
    }
    return new UserRoleApi().addUserRole(newRoleSkeleton)
      .then(({ newId }) => {
        const newRole = {
          ...newRoleSkeleton,
          id: newId
        }
        this.props.onRoleAdded(newRole)
        this.props.onRoleSelected(newRole)
      })
      .catch(toastifyError)
  }

  render () {
    return <CardsContainer onCreate={this.onCreate}>{
      this.getSortedRoles().map<React$Node>(this.getCardForRole)
    }</CardsContainer>
  }

  getSortedRoles (): UserRoleType[] {
    return sortBy(Array.from(this.props.roles.values()), role => role.name)
  }

  getCardForRole = (role: UserRoleType) => {
    const { selection, roles, onRoleAdded, ...cardProps } = this.props
    return <UserRoleCard {...cardProps} key={role.id} userRole={role} selected={role === selection}/>
  }
}
