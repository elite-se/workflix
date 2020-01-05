// @flow

import React from 'react'
import UserApi from '../../../modules/api/UsersApi'
import type { UserType } from '../../../modules/datatypes/User'
import withPromiseResolver from '../../../modules/app/hocs/withPromiseResolver'
import Filters from './filtering/Filters'
import type { FiltersType } from '../../../modules/datatypes/Filters'
import ProcessList from './ProcessList'
import ProcessGroupsApi from '../../../modules/api/ProcessGroupsApi'
import type { ProcessGroupType } from '../../../modules/datatypes/ProcessGroup'

type PropsType = {|
  users: Map<string, UserType>,
  processGroups: Map<number, ProcessGroupType>,
  path: string
|}
type StateType = {|
  filters: FiltersType
|}

class TasksOverview extends React.Component<PropsType, StateType> {
  state = {
    filters: {}
  }

  onFiltersChanged = (newFilters: FiltersType) => {
    this.setState({ filters: newFilters })
  }

  render () {
    return <div>
      <Filters onFiltersChanged={this.onFiltersChanged} filters={this.state.filters}
               users={this.props.users} processGroups={this.props.processGroups}/>
      <ProcessList filters={this.state.filters} users={this.props.users}/>
    </div>
  }
}

const promiseCreator = () => Promise.all([
  new UserApi().getUsers(),
  new ProcessGroupsApi().getProcessGroups()
])
  .then(([users, processGroups]) => ({
    users,
    processGroups
  })
  )

export default withPromiseResolver<PropsType, *>(promiseCreator)(TasksOverview)
