// @flow

import React from 'react'
import UserApi from '../../../modules/api/UsersApi'
import type { UserType } from '../../../modules/datatypes/User'
import withPromiseResolver from '../../../modules/app/hocs/withPromiseResolver'
import type { FiltersType } from '../../../modules/datatypes/Filters'
import ProcessGroupsApi from '../../../modules/api/ProcessGroupsApi'
import type { ProcessGroupType } from '../../../modules/datatypes/ProcessGroup'
import Filters from '../../tasks/components/filtering/Filters'
import ProcessTable from './ProcessTable'
import { Button, Popover } from '@blueprintjs/core'
import StartProcessChooseTemplate from './StartProcessChooseTemplate'

type PropsType = {|
  users: Map<string, UserType>,
  processGroups: Map<number, ProcessGroupType>
|}
type StateType = {|
  filters: FiltersType
|}

class ProcessesOverview extends React.Component<PropsType, StateType> {
  state = {
    filters: {}
  }

  onFiltersChanged = (newFilters: FiltersType) => {
    this.setState({ filters: newFilters })
  }

  render () {
    const { users, processGroups } = this.props
    const { filters } = this.state
    return <div style={{
      maxWidth: '100%',
      boxSizing: 'border-box',
      flex: 1
    }}>
      <div style={{
        display: 'flex',
        justifyContent: 'space-between',
        flexDirection: 'row'
      }}>
        <Filters onFiltersChanged={this.onFiltersChanged} filters={filters}
                 users={users} processGroups={processGroups}/>
        <Popover position='bottom'>
          <Button text='Start Process...' icon='play' intent='success'/>
          <StartProcessChooseTemplate processGroups={processGroups}/>
        </Popover>
      </div>
      <ProcessTable filters={filters} users={users} processGroups={processGroups}/>
    </div>
  }
}

const promiseCreator = () => Promise.all([
  new UserApi().getUsers(),
  new ProcessGroupsApi().getProcessGroups()
]).then(([users, processGroups]) => ({
  users,
  processGroups
}))

export default withPromiseResolver<PropsType, *>(promiseCreator)(ProcessesOverview)
