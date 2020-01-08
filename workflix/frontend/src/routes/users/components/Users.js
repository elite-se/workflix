// @flow

import React from 'react'
import { H2 } from '@blueprintjs/core'
import withPromiseResolver from '../../../modules/app/hocs/withPromiseResolver'
import type { UserRoleType, UserType } from '../../../modules/datatypes/User'
import UsersApi from '../../../modules/api/UsersApi'
import UserCard from './UserCard'
import { sortBy } from 'lodash'
import styled from 'styled-components'
import ProcessGroupsApi from '../../../modules/api/ProcessGroupsApi'
import type { ProcessGroupType } from '../../../modules/datatypes/ProcessGroup'
import ProcessGroupCard from './ProcessGroupCard'
import UserRoleCard from './UserRoleCard'

const CardsContainer = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex-flow: row || wrap;
  justify-content: center;
  align-items: flex-start;
  align-content: flex-start;
`

type PropsType = {|
  users: Map<string, UserType>,
  processGroups: Map<number, ProcessGroupType>,
  roles: Map<number, UserRoleType>
|}

type StateType = {|
  mode: 'USERS' | 'GROUPS' | 'ROLES'
|}

class Users extends React.Component<PropsType, StateType> {
  state = { mode: 'USERS' }

  onProcessGroupSelected = (group: ProcessGroupType) => {
    this.setState({ mode: 'GROUPS' })
  }

  onRoleSelected = (role: UserRoleType) => {
    this.setState({ mode: 'ROLES' })
  }

  onUserSelected = (user: UserType) => {
    this.setState({ mode: 'USERS' })
  }

  render () {
    const { processGroups, roles, users } = this.props
    return <div>
      <H2 style={{ textAlign: 'center' }}>All {
        this.state.mode === 'ROLES' ? 'roles' : this.state.mode === 'GROUPS' ? 'process groups' : 'users'
      }</H2>
      <CardsContainer>
        {
          this.state.mode === 'ROLES'
            ? sortBy(Array.from(roles.values()), user => user.name)
              .map(role => <UserRoleCard key={role.id} userRole={role} users={users}
                                         onUserSelected={this.onUserSelected}/>)
            : this.state.mode === 'GROUPS'
            ? sortBy(Array.from(processGroups.values()), group => group.title)
              .map(group => <ProcessGroupCard key={group.id} processGroup={group} users={users}
                                              onUserSelected={this.onUserSelected}/>)
            : sortBy(Array.from(users.values()), user => user.name)
              .map(user => <UserCard key={user.id} user={user} processGroups={processGroups} roles={roles}
                                     onProcessGroupSelected={this.onProcessGroupSelected}
                                     onRoleSelected={this.onRoleSelected}/>)
        }
      </CardsContainer>
    </div>
  }
}

const promiseCreator = () => Promise.all([
  new UsersApi().getUsers(),
  new ProcessGroupsApi().getProcessGroups(),
  new UsersApi().getUserRoles()
])
  .then(([users, processGroups, roles]) => ({
    users,
    processGroups,
    roles
  }))

export default withPromiseResolver<*, *>(promiseCreator)(Users)
