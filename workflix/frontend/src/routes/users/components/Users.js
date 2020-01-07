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

const UsersContainer = styled<{}, {}, 'div'>('div')`
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

class Users extends React.Component<PropsType> {
  render () {
    const { processGroups, roles } = this.props
    const users = sortBy(Array.from(this.props.users.values()), user => user.name)
    return <div>
      <H2 style={{ textAlign: 'center' }}>All Users</H2>
      <UsersContainer>
      {
        users.map(user => <UserCard key={user.id} user={user} processGroups={processGroups} roles={roles}/>)
      }
      </UsersContainer>
    </div>
  }
}

const promiseCreator = () => Promise.all([
  new UsersApi().getUsers(),
  new ProcessGroupsApi().getProcessGroups(),
  new UsersApi().getUserRoles()
])
  .then(([users, processGroups, roles]) => ({ users, processGroups, roles }))

export default withPromiseResolver<*, *>(promiseCreator)(Users)
