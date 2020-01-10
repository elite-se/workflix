// @flow

import React from 'react'
import UserApi from '../../../modules/api/UsersApi'
import type { UserRoleType, UserType } from '../../../modules/datatypes/User'
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
  processGroups: Map<number, ProcessGroupType>,
  userRoles: Map<number, UserRoleType>
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
    const { users, processGroups, userRoles } = this.props
    const { filters } = this.state
    return <div style={{
      maxWidth: '100%',
      display: 'flex',
      flexDirection: 'column',
      boxSizing: 'border-box',
      flex: 1,
      paddingRight: this.state.drawerOpen ? Drawer.SIZE_SMALL : '0',
      transition: 'padding-right 0.3s'
    }}>
      <Filters onFiltersChanged={this.onFiltersChanged} filters={filters} users={users} processGroups={processGroups}/>
      <ProcessList filters={filters} userRoles={userRoles} users={users} processGroups={processGroups}
                   onTaskSelected={this.onTaskSelected}/>
    </div>
  }
}

const promiseCreator = () => Promise.all([
  new UserApi().getUsers(),
  new UserApi().getUserRoles(),
  new ProcessGroupsApi().getProcessGroups()
]).then(([users, userRoles, processGroups]) => ({
  users,
  userRoles,
  processGroups
}))

export default withPromiseResolver<PropsType, *>(promiseCreator)(TasksOverview)
