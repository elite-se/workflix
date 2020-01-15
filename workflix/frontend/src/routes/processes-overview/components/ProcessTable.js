// @flow

import React from 'react'
import type { ProcessType } from '../../../modules/datatypes/Process'
import ProcessApi from '../../../modules/api/ProcessApi'
import { Colors, HTMLTable } from '@blueprintjs/core'
import styled from 'styled-components'
import type { UserType } from '../../../modules/datatypes/User'
import withPromiseResolver from '../../../modules/app/hocs/withPromiseResolver'
import type { FiltersType } from '../../../modules/datatypes/Filters'
import type { ProcessGroupType } from '../../../modules/datatypes/ProcessGroup'
import ProcessProgress from '../../../modules/common/components/ProcessProgress'
import { navigate } from '@reach/router'
import { orderBy } from 'lodash'
import SortingHeader from './SortingHeader'

const ProcessTableWrapper = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex: 1;
  justify-content: center;
  flex-direction: row;
`

type PropsType = {|
  filters: FiltersType,
  processes: Array<ProcessType>,
  processGroups: Map<number, ProcessGroupType>,
  users: Map<string, UserType>
|}

type StateType = {|
  orderName: string,
  orderDir: 'asc' | 'desc'
|}

const ProgressTranslation = {
  RUNNING: '1',
  CLOSED: '2',
  ABORTED: '0'
}

class ProcessList extends React.Component<PropsType, StateType> {
  state = {
    orderName: 'id',
    orderDir: 'asc'
  }

  navigateToProcess = (process: ProcessType) => () => navigate(`processes/${process.id}`)

  setSorting = (orderName: string, orderDir: 'asc' | 'desc') => this.setState({
    orderName,
    orderDir
  })

  render () {
    const { processes, users, processGroups } = this.props
    const { orderName, orderDir } = this.state
    return <div style={{
      maxWidth: '100%',
      overflowX: 'auto',
      display: 'flex'
    }}>
      <ProcessTableWrapper>
        {
          processes.length === 0
            ? <span style={{
              color: Colors.GRAY2,
              alignItem: 'stretch'
            }}>There are no processes matching the filters.</span>
            : <HTMLTable bordered striped interactive style={{ width: '100%' }}>
              <thead>
              <tr>
                <SortingHeader name='id' title='Id' setSorting={this.setSorting} orderDir={orderDir}
                               orderName={orderName}/>
                <SortingHeader name='title' title='Title' setSorting={this.setSorting} orderDir={orderDir}
                               orderName={orderName}/>
                <SortingHeader name='startedAt' title='Started at' setSorting={this.setSorting} orderDir={orderDir}
                               orderName={orderName}/>
                <SortingHeader name='deadline' title='Deadline' setSorting={this.setSorting} orderDir={orderDir}
                               orderName={orderName}/>
                <SortingHeader name='progress' title='Progress' setSorting={this.setSorting} orderDir={orderDir}
                               orderName={orderName}/>
                <SortingHeader name='processGroup' title='Process Group' setSorting={this.setSorting}
                               orderDir={orderDir}
                               orderName={orderName}/>
                <SortingHeader name='starter' title='Starter' setSorting={this.setSorting} orderDir={orderDir}
                               orderName={orderName}/>
              </tr>
              </thead>
              <tbody>
              {orderBy(processes.map(process => ({
                id: process.id,
                title: process.title,
                startedAt: process.startedAt,
                deadline: process.deadline, // eslint-disable-next-line no-magic-numbers
                progress: ProgressTranslation[process.status] + process.progress.toString().padStart(3, '0'),
                processGroup: processGroups.get(process.processGroupId)?.title || '',
                starter: users.get(process.starterId)?.name || '',
                process: process
              })), [orderName], [orderDir]).map(item => (
                <tr key={item.id} onClick={this.navigateToProcess(item.process)}>
                  <td>{item.id}</td>
                  <td>{item.title}</td>
                  <td>{item.startedAt.toLocaleString()}</td>
                  <td>{item.deadline.toLocaleDateString()}</td>
                  <td><ProcessProgress process={item.process}/></td>
                  <td>{item.processGroup}</td>
                  <td>{item.starter}</td>
                </tr>
              ))}
              </tbody>
            </HTMLTable>
        }</ProcessTableWrapper>
    </div>
  }
}

const promiseCreator = (props: *, refresh) =>
  new ProcessApi().getProcesses(props.filters)
    .then(processes => Promise.all([
      Promise.resolve(processes),
      new ProcessApi().getTaskTemplatesForProcessTemplates(processes.map(proc => proc.processTemplateId))
    ]))
    .then(
      ([processes, taskTemplates]) => ({
        processes,
        taskTemplates,
        refresh
      })
    )

const shouldUpdate = (oldProps: *, newProps: *) => oldProps.filters !== newProps.filters

export default withPromiseResolver<PropsType, *>(promiseCreator, shouldUpdate)(ProcessList)
