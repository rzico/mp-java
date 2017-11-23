const areas=
[
[#list areas as p]
    [#if p_index==0]
       {id:"${p.id}",name:"${p.name}",children:[
          [#list p.children as c]
              [#if c_index==0]
                {id:"${c.id}",name:"${c.name}",children:[
                  [#list c.children as r]
                      [#if r_index==0]
                          {id:"${r.id}",name:"${r.name}"}
                      [#else]
                         ,{id:"${r.id}",name:"${r.name}"}
                      [/#if]
                  [/#list]
              ]}
              [#else]
               ,{id:"${c.id}",name:"${c.name}",children:[
                  [#list c.children as r]
                      [#if r_index==0]
                       {id:"${r.id}",name:"${r.name}"}
                      [#else]
                      ,{id:"${r.id}",name:"${r.name}"}
                      [/#if]
                  [/#list]
              ]}
              [/#if]
          [/#list]
       ]}
    [#else]
    ,{id:"${p.id}",name:"${p.name}",children:[
        [#list p.children as c]
            [#if c_index==0]
            {id:"${c.id}",name:"${c.name}",children:[
                [#list c.children as r]
                    [#if r_index==0]
                     {id:"${r.id}",name:"${r.name}"}
                    [#else]
                    ,{id:"${r.id}",name:"${r.name}"}
                    [/#if]
                [/#list]
            ]}
            [#else]
            ,{id:"${c.id}",name:"${c.name}",children:[
                [#list c.children as r]
                    [#if r_index==0]
                     {id:"${r.id}",name:"${r.name}"}
                    [#else]
                    ,{id:"${r.id}",name:"${r.name}"}
                    [/#if]
                [/#list]
            ]}
            [/#if]
        [/#list]
    ]}
    [/#if]
[/#list]
]