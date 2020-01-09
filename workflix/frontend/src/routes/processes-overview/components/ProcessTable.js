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

class ProcessList extends React.Component<PropsType> {
  navigateToProcess = (process: ProcessType) => () => navigate(`processes/${process.id}`)

  render () {
    const { processes, users, processGroups } = this.props
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
              <th>Id</th>
              <th>Title</th>
              <th>Started at</th>
              <th>Progress</th>
              <th>Process Group</th>
              <th>Starter</th>
              </thead>
              <tbody>
              {processes.map(process => (
                <tr key={process.id} onClick={this.navigateToProcess(process)}>
                  <td>{process.id}</td>
                  <td>{process.title}</td>
                  <td>{process.startedAt.toLocaleString()}</td>
                  <td><ProcessProgress process={process}/></td>
                  <td>{processGroups.get(process.processGroupId)?.title || ''}</td>
                  <td>{users.get(process.starterId)?.name || ''}</td></tr>
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
