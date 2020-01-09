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
import type { TaskType } from '../../../modules/datatypes/Task'
import { Drawer } from '@blueprintjs/core'
import { getCurrentUserId } from '../../../modules/common/tokenStorage'

type PropsType = {|
  users: Map<string, UserType>,
  processGroups: Map<number, ProcessGroupType>
|}
type StateType = {|
  filters: FiltersType,
  drawerOpen: boolean
|}

class TasksOverview extends React.Component<PropsType, StateType> {
  state = {
    filters: {
      status: ['RUNNING'],
      involving: this.props.users.get(getCurrentUserId())
    },
    drawerOpen: false
  }

  onFiltersChanged = (newFilters: FiltersType) => {
    this.setState({ filters: newFilters })
  }

  onTaskSelected = (selectedTask: ?TaskType) =>
    this.setState({ drawerOpen: Boolean(selectedTask) })

  render () {
    return <div style={{
      maxWidth: '100%',
      boxSizing: 'border-box',
      flex: 1,
      paddingRight: this.state.drawerOpen ? Drawer.SIZE_SMALL : '0',
      transition: 'padding-right 0.3s'
    }}>
      <Filters onFiltersChanged={this.onFiltersChanged} filters={this.state.filters}
               users={this.props.users} processGroups={this.props.processGroups}/>
      <ProcessList filters={this.state.filters} users={this.props.users} onTaskSelected={this.onTaskSelected}/>
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
